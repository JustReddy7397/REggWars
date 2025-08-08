package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.model.game.timer.GameStartTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.Timer;

/**
 * @author JustReddy
 */
public class GameStartPhase extends GamePhase {

    @Override
    public void onEnable(Game game) {
        game.setGameState(GameState.STARTING);
        super.onEnable(game);
        final Timer timer = game.getStartTimer();

        if (timer.isStarted()) {
            timer.stop();
        }

        timer.start();

    }

    @Override
    public void onTick(Game game) {
        final Timer timer = game.getStartTimer();

        if (game.getPlayerCount() < game.getMinPlayers()) {
            timer.stop();
            game.getGamePhaseManager().setPhase(new GameWaitingPhase());
            return;
        }

        if (timer.getTicksExceed() <= 0) {
            timer.stop();
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
