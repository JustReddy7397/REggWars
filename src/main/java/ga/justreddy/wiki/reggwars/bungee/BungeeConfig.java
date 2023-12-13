package ga.justreddy.wiki.reggwars.bungee;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * @author JustReddy
 */
public class BungeeConfig {

    private static final String VERSION_KEY = "config-version";

    @Getter
    private final File file;

    @Getter
    private Configuration config;


    public BungeeConfig(String name) throws IOException {
        String finalName = name.endsWith(".yml") ? name : name + ".yml";
        File file = new File(Bungee.getInstance().getDataFolder().getAbsolutePath(), finalName);

        if (!file.exists()) {
            Bungee.getInstance().getDataFolder().mkdir();
            Files.copy(Bungee.getInstance().getResourceAsStream(finalName), file.toPath());
        }

        this.file = file;
        this.config =  ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        reload();
    }

    public void reload() throws IOException {
        this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }

    public void save() throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
    }

    public boolean isOutdated(final int currentVersion) {
        return config.getInt(VERSION_KEY, -1) < currentVersion;
    }


}
