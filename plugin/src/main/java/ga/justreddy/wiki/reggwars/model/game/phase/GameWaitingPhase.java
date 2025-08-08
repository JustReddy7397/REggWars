package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.Util;

/**
 * @author JustReddy
 */
public class GameWaitingPhase extends GamePhase {

    @Override
    public void onEnable(Game game) {
        game.setGameState(GameState.WAITING);
        super.onEnable(game);
    }

    @Override
    public void onTick(Game game) {

        if (game.getPlayerCount() >= game.getMinPlayers()) {
            game.goToNextPhase();
        }

    }

    @Override
    public void onDisable(Game game) {

    }

    @Override
    public GamePhase getNextPhase() {
        return null;
    }
}
