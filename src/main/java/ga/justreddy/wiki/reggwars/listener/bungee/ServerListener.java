package ga.justreddy.wiki.reggwars.listener.bungee;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.bungee.Core;
import ga.justreddy.wiki.reggwars.bungee.ServerMode;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * @author JustReddy
 */
public class ServerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerListPing(ServerListPingEvent event) {
/*        if (Core.MODE == ServerMode.LOBBY) {
            event.setMotd("LOBBY ; ");
        } else {
            IGame game = GameManager.getManager().getGames()
                    .values().stream().findFirst().orElse(null);
            if (game == null || game.isGameState(GameState.DISABLED)) {
                event.setMotd("");
                return;
            }

            event.setMotd(game.getGameMode().name() + " ; " + game.getName() + " ; " + game.getGameState().name());

        }*/
    }

}
