package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.model.language.Language;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class LanguageManager {

    private static LanguageManager manager;

    public static LanguageManager getManager() {
        return manager == null ? manager = new LanguageManager() : manager;
    }

    private final Map<String, ILanguage> languages;
    private final File folder;

    public LanguageManager() {
        this.folder = new File(REggWars.getInstance().getDataFolder(), "languages");
        if (!this.folder.exists()) this.folder.mkdirs();
        this.languages = new HashMap<>();
        File defaultLanguage = new File(folder.getAbsolutePath() + "/language_en.yml");
        if (!defaultLanguage.exists()) {
            REggWars.getInstance()
                    .saveResource("languages/language_en.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(defaultLanguage);
        String id = config.getString("settings.id");
        String displayname = config.getString("settings.displayname");
        register(id, config);
        ChatUtil.sendConsole("&7[&dREggWars&7] &aRegistered language: " + id + " (" + displayname + ")");
    }

    public void start() {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (!name.endsWith(".yml")) continue;
            name = name.replaceAll(".yml", "");
            if (name.equalsIgnoreCase("language_en")) continue;
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String id = config.getString("settings.id");
            String displayname = config.getString("settings.displayname");
            if (id == null) {
                ChatUtil.sendConsole("&7[&dREggWars&7] &cFailed to load " + name + "! ID is null!");
                continue;
            }

            if (displayname == null) {
                ChatUtil.sendConsole("&7[&dREggWars&7] &cFailed to load " + name + "! DISPLAYNAME is null!");
                continue;
            }

            if (languages.containsKey(id)) {
                ChatUtil.sendConsole("&7[&dREggWars&7] &cFailed to load " + name + "! ID already exists!");
                continue;
            }
            register(id, config);
            ChatUtil.sendConsole("&7[&dREggWars&7] &aRegistered language: " + id + " (" + displayname + ")");
        }

        ChatUtil.sendConsole("&7[&dREggWars&7] &aLoaded " + languages.size() + " languages!");

    }

    public void reload() {
        stop();
        File defaultLanguage = new File(REggWars.getInstance().getDataFolder().getAbsolutePath() + "/language_en.yml");
        if (!defaultLanguage.exists()) {
            REggWars.getInstance()
                    .saveResource("languages/language_en.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(defaultLanguage);
        String id = config.getString("settings.id");
        String displayname = config.getString("settings.displayname");
        register(id, config);
        ChatUtil.sendConsole("&7[&dREggWars&7] &aRegistered language: " + id + " (" + displayname + ")");
        start();
    }

    public void stop() {
        languages.clear();
    }

    private void register(String id, FileConfiguration configuration) {
        languages.put(id, new Language(configuration, id));
    }

    public ILanguage getLanguage(String id) {
        return languages.getOrDefault(id, null);
    }

}
