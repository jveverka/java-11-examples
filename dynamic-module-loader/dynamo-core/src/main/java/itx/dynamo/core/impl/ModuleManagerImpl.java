package itx.dynamo.core.impl;

import itx.dynamo.core.api.DynamoConfiguration;
import itx.dynamo.core.api.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ResolvedModule;
import java.util.Set;
import java.util.stream.Collectors;

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
        LOG.info("initializing ...");
        ModuleFinder finder = ModuleFinder.of(configuration.getDirectories());
        finder.findAll().forEach( m -> {
            System.out.format("%s\n", m.descriptor().name());

        });

        /*
        Configuration parent = ModuleLayer.boot().configuration();

        Configuration cf = parent.resolve(finder, ModuleFinder.of(), Set.of("myapp"));
        cf.modules().forEach(m -> {
            System.out.format("%s -> %s%n",
                    m.name(),
                    m.reads().stream()
                            .map(ResolvedModule::name)
                            .collect(Collectors.joining(", ")));
        });
        */
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
