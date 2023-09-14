package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public class EggWarsGameStartEvent extends EggWarsEvent {

    private final IGame game;

    public EggWarsGameStartEvent(IGame game) {
        this.game = game;
    }

    public IGame getGame() {
        return game;
    }
}
