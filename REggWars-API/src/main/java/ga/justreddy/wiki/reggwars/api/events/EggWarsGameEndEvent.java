package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;

/**
 * @author JustReddy
 */
public class EggWarsGameEndEvent extends EggWarsEvent {

    private final IGame game;
    private final IGameTeam winners;

    public EggWarsGameEndEvent(IGame game, IGameTeam winners) {
        this.game = game;
        this.winners = winners;
    }

    public IGame getGame() {
        return game;
    }

    public IGameTeam getWinners() {
        return winners;
    }
}
