package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
@Getter
public class GameManager {

    private static GameManager manager;

    public static GameManager getManager() {
        return manager == null ? manager = new GameManager() : manager;
    }

    private final File gamesFolder;

    private final Map<String, IGame> games;

    private GameManager() {
        this.gamesFolder = new File(REggWars.getInstance()
                .getDataFolder().getAbsolutePath() + "/data/games/");
        if (!gamesFolder.exists()) gamesFolder.mkdir();
        this.games = new HashMap<>();
    }

    public void start() {
        File[] files = gamesFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (games.containsKey(name)) continue;
            register(name, YamlConfiguration.loadConfiguration(file));
        }
        ChatUtil.sendConsole("&7[&dREggWars&7] &aLoaded &l" + games.size() + "&r &agames");
    }

    public void register(String name, FileConfiguration configuration) {
        Game game = new Game(name, configuration);
        games.put(name, game);
        REggWars.getInstance().getResetAdapter().onEnable(game);
    }

    public void reload() {
        end();
        start();
    }

    public void end() {
        games.clear();
    }


    public IGame getGameByName(String name) {
        return games.getOrDefault(name, null);
    }


}
