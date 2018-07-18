package itx.dynamo.core.impl;

import itx.dynamo.core.api.DynamoConfiguration;
import itx.dynamo.core.api.ModuleInfo;
import itx.dynamo.core.api.ModuleManager;
import itx.dynamo.services.api.ModuleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ModuleManagerImpl implements ModuleManager {

    final private static Logger LOG = LoggerFactory.getLogger(ModuleManagerImpl.class);

    private final DynamoConfiguration configuration;
    private final ServiceRegistryImpl serviceRegistry;

    public ModuleManagerImpl(DynamoConfiguration configuration) {
        this.configuration = configuration;
        this.serviceRegistry = new ServiceRegistryImpl();
    }

    @Override
    public void initialize() {

        //https://docs.oracle.com/javase/9/docs/api/java/lang/module/Configuration.html
        //https://docs.oracle.com/javase/9/docs/api/java/lang/ModuleLayer.html

        LOG.info("initializing ...");
        Set<String> modulesNames = new HashSet<>();

        ModuleFinder finder = ModuleFinder.of(this.configuration.getDirectories());
        finder.findAll().forEach( m -> {
            System.out.format("%s\n", m.descriptor().name());
            modulesNames.add(m.descriptor().name());
        });

        ModuleLayer moduleLayer = ModuleLayer.boot();
        Configuration configuration = moduleLayer.configuration().resolve(finder, ModuleFinder.of(), modulesNames);
        ClassLoader systemClassLoader = ClassLoader.getPlatformClassLoader();
        ModuleLayer newModuleLayer = moduleLayer.defineModulesWithOneLoader(configuration, systemClassLoader);

        finder.findAll().forEach( m -> {
            try {
                LOG.info("loading {}", m.descriptor().name());
                ModuleInfo mi = this.configuration.getModuleInfo(m.descriptor().name());
                if (mi != null) {
                    LOG.info("initializing {}/{}", mi.getName(), mi.getInitializer());
                    Class<?> c = newModuleLayer.findLoader(mi.getName()).loadClass(mi.getInitializer());
                    Object object = c.getConstructors()[0].newInstance();
                    ModuleHandler moduleHandler = (ModuleHandler)object;
                    LOG.info("created instance of {}", mi.getInitializer());
                }
            } catch (ClassNotFoundException e) {
                LOG.error("Error: ", e);
            } catch (IllegalAccessException e) {
                LOG.error("Error: ", e);
            } catch (InstantiationException e) {
                LOG.error("Error: ", e);
            } catch (InvocationTargetException e) {
                LOG.error("Error: ", e);
            }
        });
        LOG.info("initialization done.");
    }

    @Override
    public ServiceRegistryImpl getServiceRegistryImpl() {
        return serviceRegistry;
    }

    @Override
    public void close() throws Exception {
        LOG.info("closing ...");
    }

}
