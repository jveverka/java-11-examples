package itx.dynamo.app;

import itx.dynamo.core.api.Dynamo;
import itx.dynamo.core.api.DynamoConfiguration;
import itx.dynamo.core.api.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    final private static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String ... args) throws Exception {
        LOG.info("Starting Main");
        DynamoConfiguration dynamoConfiguration = Dynamo.loadConfiguration(Main.class.getResourceAsStream("/app-configuration.json"));
        dynamoConfiguration.getDirectories();
        ModuleManager moduleManager = Dynamo.getModuleManager(dynamoConfiguration);
        moduleManager.initialize();
        moduleManager.close();
    }

}
