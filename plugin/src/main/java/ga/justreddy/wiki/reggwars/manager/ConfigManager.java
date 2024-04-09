package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.utils.ConfigWriter;
import lombok.SneakyThrows;
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

    public Config get(String name) {
        return configs.getOrDefault(name, null);
    }

    public void set(SerializableConfig config) {
        Config c = get(config.getConfig());
        if (c == null) return;
        set(c, config.getObj());
    }

    @SneakyThrows
    public void set(File file, FileConfiguration config, Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            config.set(key, value);
        }
        config.save(file);
    }

    public void set(Config config, Map<String, Object> obj) {
        set(config.getFile(), config.getConfig(), obj);
    }

    private String repeat(int spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }

        return sb.toString();
    }

    @SneakyThrows
    public void reload() {
        for (Config config : configs.values()) {
            config.reload();
        }

        if (!REggWars.getInstance().isBungee()) return;
        configs.forEach((string, config) -> {
            REggWars.getInstance().getMessenger()
                    .getSender()
                    .sendConfigUpdatePacket(REggWars.getInstance().getServerName(), config);
        });
    }

    public Map<String, Config> getConfigs() {
        return configs;
    }


}
