package ga.justreddy.wiki.reggwars.commands.sub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.bungee.Core;
import ga.justreddy.wiki.reggwars.bungee.ServerMode;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.game.Game;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public class JoinCommand extends Command {

    public JoinCommand(REggWars plugin) {
        super(plugin);
        setName("join");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {


        if (Core.MODE == ServerMode.LOBBY) {
            // TODO yay
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("SOLO"); // TODO :o
            output.writeUTF("all");
            Player player = gamePlayer.getPlayer();
            player.sendPluginMessage(REggWars.getInstance(), "REggWarsAPI", output.toByteArray());
        } else if (Core.MODE == ServerMode.BUNGEE){
            // TODO HAHA
            gamePlayer.sendLegacyMessage("&cYou are already in-game!");
        } else {
            // TODO WOW

            if (gamePlayer.getGame() != null && !gamePlayer.isDead()) {
                gamePlayer.sendLegacyMessage("&cYou are already in-game");
                return;
            }

            IGame game = GameManager.getManager().getRandomGame();
            if (game != null) {
                game.onGamePlayerJoin(gamePlayer);
            } else {
                // TODO
                gamePlayer.sendLegacyMessage("&cFailed to find active game!");
            }

        }

    }
}
