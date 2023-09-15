package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;

/**
 * @author JustReddy
 */
public class EggWarsGameJoinEvent extends EggWarsEvent {

    private final IGame game;
    private final IGamePlayer player;

    public EggWarsGameJoinEvent(IGame game, IGamePlayer player) {
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
