package ga.justreddy.wiki.reggwars.socket;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.classes.BackToServerPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class JoinRunnable {

    private final REggWars plugin;
    int timesChecked = 200;

    public JoinRunnable() {
        plugin = REggWars.getInstance();
    }

    public void runTask(String playerName, BungeeGame game) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player != null && Bukkit.getOnlinePlayers().contains(player)) {
                    if (REggWars.getInstance().getSocketClient().isDebug()) {
                        REggWars.getInstance().getSocketClient().log(Level.INFO, "[X] Player " + player.getName() + " joined game " + game.getName() + " successfully!");
                    }
                    IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
                    if (GameManager.getManager().getGameByName(game.getName()) == null) {
                        BungeeUtils.getInstance().sendBackToServer(gamePlayer);
                        cancel();
                        return;
                    }
                    GameManager.getManager().getGameByName(game.getName()).onGamePlayerJoin(gamePlayer);
                    this.cancel();
                } else if (timesChecked > 0) {
                    timesChecked--;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
