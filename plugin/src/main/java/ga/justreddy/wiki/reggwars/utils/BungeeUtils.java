package ga.justreddy.wiki.reggwars.utils;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.packets.socket.classes.BackToServerPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public class BungeeUtils {

    private static BungeeUtils instance;

    public static BungeeUtils getInstance() {
        if (instance == null) {
            instance = new BungeeUtils();
        }
        return instance;
    }

    private final REggWars plugin;

    private BungeeUtils() {
        plugin = REggWars.getInstance();
    }

    public void sendBackToServer(IGamePlayer player) {
        Player bukkitPlayer = player.getPlayer();
        if (bukkitPlayer == null) return;

        switch (plugin.getSettingsConfig().getConfig().getString("bungee.on-leave")) {
            case "JOIN_SERVER": {
                plugin.getMessenger().getSender()
                        .sendBackToServerPacket(BackToServerPacket.ServerPriority.PREVIOUS,
                                player.getName(),
                                plugin.getSettingsConfig().getConfig().getString("bungee.lobby"));
                break;
            }
            case "LOBBY_SERVER": {
                plugin.getMessenger().getSender()
                        .sendBackToServerPacket(BackToServerPacket.ServerPriority.LOBBY,
                                player.getName(),
                                plugin.getSettingsConfig().getConfig().getString("bungee.lobby"));
                break;
            }
            case "LEAVE_COMMAND": {
                for (String s : plugin.getConfig().getStringList("bungee.leave-commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("<player>", player.getName()));
                }
                break;
            }
        }

    }


}
