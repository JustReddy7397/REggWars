package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;

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
            joinBungeeGame(gamePlayer);
        } else if (Core.MODE == ServerMode.BUNGEE){
            // TODO HAHA
            IGame game = gamePlayer.getGame();
            /*if (game != null) {
                // It should NEVER be null but just in case
                game.onGamePlayerQuit(gamePlayer, game.isGameState(GameState.PLAYING) || game.isGameState(GameState.ENDING));
            }*/
            // TODO quit game if in game
            joinBungeeGame(gamePlayer);

        } else {
            // TODO WOW

            if (gamePlayer.getGame() != null && !gamePlayer.isDead()) {
                gamePlayer.sendMessage(
                        Message.MESSAGES_ERROR_ALREADY_IN_GAME
                );
                return;
            }

            IGame game = GameManager.getManager().getRandomGame();
            if (game != null) {
                game.onGamePlayerJoin(gamePlayer);
            } else {
                gamePlayer.sendMessage(
                        Message.MESSAGES_ERROR_FAILED_TO_FIND_GAME
                );
            }

        }

    }

    private void joinBungeeGame(IGamePlayer player) {
        BungeeGame game = GameManager.getManager().getRandomBungeeGame();
        // For now it's just a solo game
        // Planning to add more data to the class later
        if (game == null) {
            // TODO
            player.sendLegacyMessage("&cFailed to find an active game for EGGWARS_SOLO");
            return;
        }

        if (game.isGameState(GameState.DISABLED)) {
            // TODO sendm essage
            return;
        }

        if (game.isGameState(GameState.PLAYING)) {
            // TODO send message
            return;
        }

        if (game.isGameState(GameState.ENDING)) {
            // TODO send message
            return;
        }

        if (game.isGameState(GameState.RESTARTING)) {
            // TODO send message
            return;
        }

        if (game.getPlayers().size() > game.getMaxPlayers()) {
            // TODO send message
            return;
        }

        // TODO

        boolean isLocalOrJoinServer = game.getServer().equals(REggWars.getInstance().getServerName());
        plugin.getMessenger().getSender()
                .sendJoinPacket(game, player.getUniqueId(), player.getName(), isLocalOrJoinServer, isLocalOrJoinServer);
    }

}
