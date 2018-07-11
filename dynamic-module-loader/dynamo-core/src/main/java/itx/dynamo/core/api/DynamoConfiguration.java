package itx.dynamo.core.api;

import java.nio.file.Path;
import java.util.Set;

public class DynamoConfiguration {

    private final Path[] directories;
    private final Set<ModuleInfo> moduleInfoSet;

    public DynamoConfiguration(Path[] directories, Set<ModuleInfo> moduleInfoSet) {
        this.directories = directories;
        this.moduleInfoSet = moduleInfoSet;
    }

    public Path[] getDirectories() {
        return directories;
    }

    public Set<ModuleInfo> getModuleInfoSet() {
        return moduleInfoSet;
    }

}
