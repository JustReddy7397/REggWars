package ga.justreddy.wiki.reggwars.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameMode;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class ServerLobbies {

    public static int SOLO;
    public static int TEAM;

    public static final Map<String, Integer> SOLO_MAP = new HashMap<>();
    public static final Map<String, Integer> TEAM_MAP = new HashMap<>();

    public static void writeToLobby(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Lobby");
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer != null) {
            // TODO save player
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(REggWars.getInstance(), () -> {
            player.sendPluginMessage(REggWars.getInstance(), "REggWarsAPI", output.toByteArray());
        }, 15);
    }

    public static void writeToCount(String serverType) {
        Player faker = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (faker != null) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();

            output.writeUTF("Count");
            output.writeUTF(serverType);

            faker.sendPluginMessage(REggWars.getInstance(), "REggWarsAPI", output.toByteArray());
        }

    }

    public static void writeToGame(Player player, String serverType, String map) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Play");
        output.writeUTF(serverType);
        output.writeUTF(map);
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer != null) {
            // TODO save player
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(REggWars.getInstance(), () -> {
            player.sendPluginMessage(REggWars.getInstance(), "REggWarsAPI", output.toByteArray());
        }, 15);

    }

    public static void writeToMapSelector(String serverType) {
        Player faker = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (faker != null) {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();

            output.writeUTF("MapSelector");
            output.writeUTF(serverType);

            faker.sendPluginMessage(REggWars.getInstance(), "REggWarsAPI", output.toByteArray());
        }
    }

    public static void setupLobbies() {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(REggWars.getInstance(), "REggWarsAPI");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;
                for (GameMode gameMode : GameMode.values()) {
                    writeToCount(gameMode.name());
                    writeToMapSelector(gameMode.name());
                }
            }
        }.runTaskTimer(REggWars.getInstance(), 0, 40);
    }

}
