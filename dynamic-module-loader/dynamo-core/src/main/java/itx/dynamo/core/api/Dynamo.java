package itx.dynamo.core.api;

import com.fasterxml.jackson.databind.JsonNode;
import itx.dynamo.core.impl.ModuleManagerImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Dynamo {

    private Dynamo() {
        throw new UnsupportedOperationException("Do not instantiate this class please !");
    }

    public static DynamoConfiguration loadConfiguration(InputStream is) throws IOException {
        try (is) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(is);
            JsonNode directories = jsonNode.get("directories");
            Path[] paths = new Path[directories.size()];
            for (int i=0; i< directories.size(); i++) {
                String dirPath = directories.get(i).textValue();
                paths[i] = Paths.get(dirPath).toAbsolutePath().normalize();
            }
            JsonNode modules = jsonNode.get("modules");
            Set<ModuleInfo> moduleInfoSet = new HashSet<ModuleInfo>();
            for (int i=0; i< modules.size(); i++) {
                JsonNode moduleNode = modules.get(i);
                ModuleInfo moduleInfo = new ModuleInfo(
                        moduleNode.get("name").asText(),
                        moduleNode.get("initializer").asText(),
                        moduleNode.get("description").asText()
                );
                moduleInfoSet.add(moduleInfo);
            }
            return new DynamoConfiguration(paths, Collections.unmodifiableSet(moduleInfoSet));
        }
    }

    public static ModuleManager getModuleManager(DynamoConfiguration configuration) {
        return new ModuleManagerImpl(configuration);
    }

}
