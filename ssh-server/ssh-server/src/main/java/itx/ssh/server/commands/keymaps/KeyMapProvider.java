package itx.ssh.server.commands.keymaps;

import java.util.HashMap;
import java.util.Map;

public class KeyMapProvider {

    private static final String DEFAULT = "default";
    private Map<String, KeyMap> keyMaps;

    public KeyMapProvider() {
        this.keyMaps = new HashMap<>();
        this.keyMaps.put(DEFAULT, new DefaultKeyMap());
    }

    public KeyMapProvider(Map<String, KeyMap> keyMaps) {
        this.keyMaps = keyMaps;
    }

    public KeyMap getDefaultKeyMap() {
        return this.keyMaps.get(DEFAULT);
    }

    public KeyMap getKeyMap(String keyMapName) {
        return this.keyMaps.get(keyMapName);
    }

    public static KeyMap createDefaultKeyMap() {
        return new DefaultKeyMap();
    }

}
