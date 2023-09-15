package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public class EggWarsGameLeaveEvent extends EggWarsEvent {

    private final IGame game;
    private final IGamePlayer player;

    public EggWarsGameLeaveEvent(IGame game, IGamePlayer player) {
        this.game = game;
        this.player = player;
    }

    public IGame getGame() {
        return game;
    }

    public IGamePlayer getPlayer() {
        return player;
    }

}
