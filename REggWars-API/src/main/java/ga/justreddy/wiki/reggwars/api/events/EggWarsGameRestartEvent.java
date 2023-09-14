package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public class EggWarsGameRestartEvent extends EggWarsEvent {

    private final IGame game;

    public EggWarsGameRestartEvent(IGame game) {
        this.game = game;
    }

    public IGame getGame() {
        return game;
    }

}
