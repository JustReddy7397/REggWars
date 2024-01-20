package ga.justreddy.wiki.reggwars.config;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.manager.ConfigManager;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author JustReddy
 */
public class Config {

    /**
     * The name of the key that stores an integer value, which is the current configuration file
     * version.
     */
    private static final String VERSION_KEY = "config-version";

    /**
     * The system file of the config.
     */
    @Getter
    private final File file;

    /**
     * The actual configuration object which is what you use for modifying and accessing the config.
     */
    @Getter
    private final FileConfiguration config;

    /**
     * Loads a YAML configuration file. If the file does not exist, a new file will be copied from the
     * project's resources folder.
     *
     * @param name The name of the config, ending in .yml
     * @throws InvalidConfigurationException If there was a formatting/parsing error in the config
     * @throws IOException                   If the file could not be created and/or loaded
     */
    public Config(final String name)
            throws InvalidConfigurationException, IOException {

        final String completeName = name.endsWith(".yml") ? name : name + ".yml";

        final File configFile = new File(REggWars.getInstance().getDataFolder().getAbsolutePath(), completeName);

        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();
            REggWars.getInstance().saveResource(completeName, false);
        }

        this.file = configFile;
        this.config = YamlConfiguration.loadConfiguration(file);
        reload();
        ConfigManager.getManager().register(this);
    }

    /**
     * Reloads the configuration file.
     *
     * @throws InvalidConfigurationException If there was a formatting/parsing error in the config
     * @throws IOException                   If the file could not be reloaded
     */
    public void reload() throws InvalidConfigurationException, IOException {
        config.load(file);
    }

    /**
     * Saves the configuration file (after making edits).
     *
     * @throws IOException If the file could not be saved
     */
    public void save() throws IOException {
        config.save(file);
    }
    /**
     * Checks if the config version integer equals or is less than the current version.
     *
     * @param currentVersion The expected value of the config version
     * @return True if the config is outdated, false otherwise
     */
    public boolean isOutdated(final int currentVersion) {
        if (config.getInt(VERSION_KEY, -1) < currentVersion) {
            ChatUtil.sendConsole("&cConfiguration file " + file.getName() + " is outdated! Shutting down plugin.");
            Bukkit.getPluginManager().disablePlugin(REggWars.getInstance());
            return true;
        }
        return false;
    }

}
