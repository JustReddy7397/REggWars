package ga.justreddy.wiki.reggwars.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
@Getter
public class SerializableConfig implements Serializable {


    private final String config;
    private final Map<String, Object> obj;

    public SerializableConfig(Config config) {
        this.config = config.getFile().getName().replace(".yml", "");
        this.obj = new HashMap<>();
        FileConfiguration configuration = config.getConfig();
        for (String key : configuration.getKeys(false)) {
            obj.put(key, configuration.get(key));
        }
    }

}
