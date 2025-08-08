package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.BungeeUtils;
import org.bukkit.Bukkit;

/**
 * @author JustReddy
 */
public class GameRestartingPhase extends GamePhase {

    @Override
    public void onEnable(Game game) {
        game.setGameState(GameState.RESTARTING);
        super.onEnable(game);
        if (game.getWorld() != null) {
            game.getWorld().getPlayers().forEach(player -> {
                if (Core.MODE == ServerMode.BUNGEE) {
                    BungeeUtils.getInstance().sendBackToServer(
                            PlayerManager.getManager().getGamePlayer(player.getUniqueId())
                    );
                    return;
                } else if (Core.MODE == ServerMode.MULTI_ARENA) {
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    // TODO
                    return;

                }
            });

            game.getAdapter().onRestart(game);
        }
    }

    @Override
    public void onTick(Game game) {

    }

    @Override
    public void onDisable(Game game) {

    }

    @Override
    public GamePhase getNextPhase() {
        return null;
    }
}
