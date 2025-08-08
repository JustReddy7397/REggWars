package ga.justreddy.wiki.reggwars.api.model.game.team;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * This interface represents a team assigner in the game.
 * It provides methods to assign teams to a game.
 *
 * @author JustReddy
 */
public interface ITeamAssigner {

    void assignTeam(IGame game);

}
