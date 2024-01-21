package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.manager.LanguageManager;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class LanguagesUpdatePacket extends Packet implements Serializable {

    private final String server;
    private final Map<String, Map<String, Object>> languages;

    public LanguagesUpdatePacket() {
        super(PacketType.LANGUAGES_UPDATE);
        this.server = REggWars.getInstance().getServerName();
        this.languages = new HashMap<>();
        for (Map.Entry<String, ILanguage> entry : LanguageManager.getManager().getLanguages().entrySet()) {
            Map<String, Object> obj = new HashMap<>();
            FileConfiguration configuration = entry.getValue().getConfig();
            for (String key : configuration.getKeys(false)) {
                obj.put(key, configuration.get(key));
            }
            languages.put(entry.getKey(), obj);
        }
    }

    public Map<String, Map<String, Object>> getLanguages() {
        return languages;
    }

    public String getServer() {
        return server;
    }
}
