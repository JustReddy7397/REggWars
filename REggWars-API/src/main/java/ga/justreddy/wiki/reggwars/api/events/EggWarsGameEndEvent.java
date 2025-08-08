package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;

/**
 * This event is called when a game ends.
 * It provides information about the game and the winning team.
 *
 * @author JustReddy
 */
public class EggWarsGameEndEvent extends EggWarsEvent {

    private final IGame game;
    private final IGameTeam winners;

    /**
     * Constructs a new EggWarsGameEndEvent with the given game and winning team.
     * @param game The game that ended
     * @param winners The winning team
     */
    public EggWarsGameEndEvent(IGame game, IGameTeam winners) {
        this.game = game;
        this.winners = winners;
    }

    /**
     * Returns the game that ended.
     * @return the game
     */
    public IGame getGame() {
        return game;
    }

    /**
     * Returns the winning team.
     * @return the winning team
     */
    public IGameTeam getWinners() {
        return winners;
    }
}
