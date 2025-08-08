package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.model.game.signs.GameSign;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class GameSignManager {

    private static GameSignManager instance;

    public static GameSignManager getInstance() {
        if (instance == null) {
            instance = new GameSignManager();
        }
        return instance;
    }

    private final Map<Location, IGameSign> signs;


    public GameSignManager() {
        this.signs = new HashMap<>();
    }

    public void start() {
        FileConfiguration config = REggWars.getInstance().getSignsConfig().getConfig();
        if (!config.contains("signs")) return;
        ConfigurationSection section = config.getConfigurationSection("signs");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            ConfigurationSection sign = section.getConfigurationSection(key);
            if (sign == null) continue;
            Location location = (Location) sign.get("location");
            if (location == null) continue;
            String gameId = sign.getString("game");
            if (gameId == null) continue;
            signs.put(location, new GameSign(
                    key,
                    location,
                    GameManager.getManager().getGameByName(gameId)
            ));
        }
    }

    public void updateSigns() {
        signs.values().forEach(IGameSign::update);
    }

    public Map<Location, IGameSign> getSigns() {
        return signs;
    }
}
