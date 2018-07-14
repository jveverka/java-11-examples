package itx.dynamo.core.api;

import java.nio.file.Path;
import java.util.*;

public class DynamoConfiguration {

    private final Path[] directories;
    private final Map<String, ModuleInfo> moduleInfos;

    public DynamoConfiguration(Path[] directories, Set<ModuleInfo> moduleInfoSet) {
        this.directories = directories;
        this.moduleInfos = new HashMap<>();
        moduleInfoSet.forEach( mi -> {
            moduleInfos.put(mi.getName(), mi);
        });
    }

    public Path[] getDirectories() {
        return directories;
    }

    public Set<ModuleInfo> getModuleInfoSet() {
        return Collections.unmodifiableSet(new HashSet<ModuleInfo>(moduleInfos.values()));
    }

    public ModuleInfo getModuleInfo(String name) {
        return moduleInfos.get(name);
    }

}
