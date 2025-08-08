package ga.justreddy.wiki.reggwars.api.model.game.team;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.Region;
import org.bukkit.Location;

import java.util.List;

/**
 * This interface represents a game team in the game.
 * It provides methods to manage the team's players and game-related information.
 *
 * @author JustReddy
 */
public interface IGameTeam {

    /**
     * Returns the unique identifier of this team.
     *
     * @return the id of this team
     */
    String getId();

    /**
     * Returns a list of all players in this team.
     *
     * @return a list of IGamePlayer
     */
    List<IGamePlayer> getPlayers();

    /**
     * Returns a list of all alive players in this team.
     *
     * @return a list of IGamePlayer
     */
    List<IGamePlayer> getAlivePlayers();

    /**
     * Returns a list of all spectator players in this team.
     *
     * @return a list of IGamePlayer
     */
    List<IGamePlayer> getSpectatorPlayers();

    /**
     * Returns the size of this team.
     *
     * @return the size of this team
     */
    int getSize();

    /**
     * Adds a player to this team.
     *
     * @param player the player to add
     */
    void addPlayer(IGamePlayer player);

    /**
     * Checks if a player is in this team.
     *
     * @param player the player to check
     * @return true if the player is in this team, false otherwise
     */
    boolean hasPlayer(IGamePlayer player);

    /**
     * Removes a player from this team.
     *
     * @param player the player to remove
     */
    void removePlayer(IGamePlayer player);

    /**
     * Returns the spawn location of this team.
     *
     * @return the spawn location
     */
    Location getSpawnLocation();

    /**
     * Returns the egg location of this team.
     *
     * @return the egg location
     */
    Location getEggLocation();

    /**
     * Checks if the egg of this team is gone.
     *
     * @return true if the egg is gone, false otherwise
     */
    boolean isEggGone();

    /**
     * Sets the egg status of this team.
     *
     * @param eggGone the new egg status
     */
    void setEggGone(boolean eggGone);

    /**
     * Returns the team of this game team.
     *
     * @return the team
     */
    Team getTeam();

    /**
     * Returns the game of this game team.
     *
     * @return the game
     */
    IGame getGame();

}
