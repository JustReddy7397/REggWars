package ga.justreddy.wiki.reggwars.bungee.config;

import com.google.common.collect.ImmutableList;
import ga.justreddy.wiki.reggwars.bungee.Bungee;
import ga.justreddy.wiki.reggwars.utils.FileUtils;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class BungeeConfig {

    @Getter
    private File file;
    private Configuration config;

    private BungeeConfig(String path, String name) {
        this.file = new File(path + "/" + name + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            InputStream in = Bungee.getInstance().getResourceAsStream(name + ".yml");
            if (in != null) {
                try {
                    FileUtils.copy(in, file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
        }

        try (InputStream istream = Files.newInputStream(file.toPath());
             InputStreamReader reader = new InputStreamReader(istream, StandardCharsets.UTF_8)
        ) {
            this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(reader);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public boolean createSection(String path) {
        this.config.set(path, new HashMap<>());
        return save();
    }

    public boolean set(String path, Object obj) {
        this.config.set(path, obj);
        return save();
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.config.getInt(path, def);
    }

    public double getDouble(String path) {
        return this.config.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.config.getDouble(path, def);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public Collection<String> getKeys(boolean flag) {
        return this.config.getKeys();
    }

    public Configuration getSection(String path) {
        return this.config.getSection(path);
    }

    public void reload() {

        try (InputStream istream = Files.newInputStream(file.toPath());
             InputStreamReader reader = new InputStreamReader(istream, StandardCharsets.UTF_8)
        ) {
            this.config = YamlConfiguration.getProvider(YamlConfiguration.class).load(reader);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public boolean save() {
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(this.config, this.file);
            return true;
        } catch (IOException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public Configuration getRawConfig() {
        return config;
    }

    private static final Map<String, BungeeConfig> cache = new HashMap<>();

    public static BungeeConfig getConfig(String name) {
        return getConfig(name, "plugins/REggWars");
    }

    public static BungeeConfig getConfig(String name, String path) {
        if (!cache.containsKey(path + "/" + name)) {
            cache.put(path + "/" + name, new BungeeConfig(path, name));
        }

        return cache.get(path + "/" + name);
    }

    public static void removeConfig(String name) {
        cache.remove("plugins/REggWars/" + name);
    }

    public static void removeConfig(BungeeConfig config) {
        for (Map.Entry<String, BungeeConfig> cu : ImmutableList.copyOf(cache.entrySet())) {
            if (cu.getValue().equals(config)) {
                cache.remove(cu.getKey());
                return;
            }
        }
    }

    public static Collection<BungeeConfig> listConfigs() {
        return cache.values();
    }


}
