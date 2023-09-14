package ga.justreddy.wiki.reggwars.api.model.game.team;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.Region;
import org.bukkit.Location;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IGameTeam {

    String getId();

    List<IGamePlayer> getPlayers();

    List<IGamePlayer> getAlivePlayers();

    List<IGamePlayer> getSpectatorPlayers();

    void addPlayer(IGamePlayer player);

    boolean hasPlayer(IGamePlayer player);

    void removePlayer(IGamePlayer player);

    Location getSpawnLocation();

    Location getEggLocation();

    boolean isEggGone();

    void setEggGone(boolean eggGone);

    Team getTeam();

    IGame getGame();

}
