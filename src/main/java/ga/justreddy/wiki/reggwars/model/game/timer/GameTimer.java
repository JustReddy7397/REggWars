package ga.justreddy.wiki.reggwars.model.game.timer;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public class GameTimer extends Timer {

    private final IGame game;

    public GameTimer(int seconds, REggWars plugin, IGame game) {
        super(seconds, plugin);
        this.game = game;
    }

    @Override
    public void run() {
        game.onCountDown();
    }

    @Override
    protected void onTick() {

    }

    @Override
    protected void onEnd() {
        stop();
    }
}
