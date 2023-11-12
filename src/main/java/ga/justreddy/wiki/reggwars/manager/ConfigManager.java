package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.utils.ConfigWriter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class ConfigManager {

    private static ConfigManager manager;

    public static ConfigManager getManager() {
        return manager == null ? manager = new ConfigManager() : manager;
    }

    private final Map<String, Config> configs;

    private ConfigManager() {
        this.configs = new HashMap<>();
    }

    public void register(Config config) {
        this.configs.put(config.getFile().getName().replace(".yml", ""), config);
    }

    public String parseConfiguration(FileConfiguration config) {
        StringBuilder sb = new StringBuilder();
        for (String key : config.getKeys(false)) {
            sb.append(parseSection(key, config.get(key), 0));
        }

        return sb.toString();
    }

    public void setConfigValues(String contents, File file) {
        ConfigWriter writer = new ConfigWriter(file);
        writer.write();
    }

    private String parseSection(String key, Object object, int spaces) {
        StringBuilder join = new StringBuilder(repeat(spaces) + key + ":");
        if (object instanceof String) {
            join.append(" '").append(((String) object)
                            .replace("'", "''")
                            .replace("§", "&")
                    )
                    .append("'\n");
        } else if (object instanceof Integer || object instanceof Double ||
                object instanceof Float || object instanceof Long
                || object instanceof Boolean) {
            join.append(" ").append(object).append("\n");
        } else if (object instanceof List) {
            join.append("\n");
            for (Object obj : (List<?>) object) {
                if (obj instanceof Integer) {
                    join.append(repeat(spaces)).append("- ").
                            append(obj.toString()).append("\n");
                } else {
                    join.append(repeat(spaces)).append("- '").append(obj.toString().replace("'", "''")).append("'\n");
                }
            }
        } else if (object instanceof ConfigurationSection) {
            ConfigurationSection section = (ConfigurationSection) object;
            if (section.getKeys(false).isEmpty()) {
                join.append(" {}\n");
            } else {
                join.append("\n");
                for (String s : section.getKeys(false)) {
                    join.append(parseSection(s, section.get(s), spaces + 1));
                }
            }
        }
        return join.toString();
    }

    private String repeat(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }

        return sb.toString();
    }

    public Map<String, Config> getConfigs() {
        return configs;
    }


}
