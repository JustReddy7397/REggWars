package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.bungee.Core;
import ga.justreddy.wiki.reggwars.bungee.ServerMode;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ShuffleUtil;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
    private final Map<String, BungeeGame> bungeeGames;

    private GameManager() {
        this.gamesFolder = new File(REggWars.getInstance()
                .getDataFolder().getAbsolutePath() + "/data/games/");
        if (!gamesFolder.exists()) gamesFolder.mkdir();
        this.games = new HashMap<>();
        this.bungeeGames = new HashMap<>();
    }

    public void start() {
        if (Core.MODE == ServerMode.LOBBY) {
            // TODO request servers if any
            return;
        }
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

    public IGame getGameByWorld(World world) {
        return games.values().stream().filter(game -> game.getWorld() == world)
                .findFirst()
                .orElse(null);
    }

    public IGame getRandomGame() {
        List<IGame> list = new ArrayList<>();
        IGame game = null;
        for (IGame g : games.values()) {
            if (g.isGameState(GameState.DISABLED)) continue;
            if (g.isGameState(GameState.WAITING) || g.isGameState(GameState.STARTING)) {
                if (g.getPlayerCount() >= g.getMaxPlayers()) {
                    continue;
                }
                list.add(g);
            }
        }

        return list.get(0);
    }

    public IGame getGameByName(String name) {
        return games.getOrDefault(name, null);
    }


    public BungeeGame getRandomBungeeGame() {
        List<BungeeGame> list = new ArrayList<>();
        BungeeGame game = null;
        for (BungeeGame g : bungeeGames.values()) {
            if (g.isGameState(GameState.DISABLED)) continue;
            if (g.isGameState(GameState.WAITING) || g.isGameState(GameState.STARTING)) {
                if (g.getPlayers().size() >= g.getMaxPlayers()) {
                    continue;
                }
                list.add(g);
            }
        }
        if (list.isEmpty()) return null;
        return list.get(0);
    }
}
