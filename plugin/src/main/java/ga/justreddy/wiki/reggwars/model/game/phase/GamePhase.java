package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.Util;

/**
 * @author JustReddy
 */
public abstract class GamePhase {

    public void onEnable(Game game) {
        Util.updateGame(game);
    }

    public abstract void onTick(Game game);

    public abstract void onDisable(Game game);

    public abstract GamePhase getNextPhase();

}
