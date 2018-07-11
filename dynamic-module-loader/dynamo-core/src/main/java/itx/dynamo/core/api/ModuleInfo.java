package itx.dynamo.core.api;

import java.util.Objects;

public class ModuleInfo {

    private final String name;
    private final String initializer;
    private final String description;

    public ModuleInfo(String name, String initializer, String description) {
        this.name = name;
        this.initializer = initializer;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getInitializer() {
        return initializer;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleInfo that = (ModuleInfo) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
