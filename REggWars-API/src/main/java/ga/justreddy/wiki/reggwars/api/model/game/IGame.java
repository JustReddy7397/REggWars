package ga.justreddy.wiki.reggwars.api.model.game;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IGame {

    String getName();

    String getDisplayName();

    List<IGamePlayer> getPlayers();

    List<IGamePlayer> getAlivePlayers();

    List<IGamePlayer> getDeadPlayers();

    int getPlayerCount();

    List<IGameTeam> getTeams();

    List<IGameTeam> getAliveTeams();

    List<IGameTeam> getDeadTeams();

    Region getGameRegion();

    List<Region> getRegions();

    void addRegion(Region region);

    void removeRegion(Region region);

    int getTeamSize();

    GameState getGameState();

    void setGameState(GameState gameState);

    GameMode getGameMode();

    void setGameMode(GameMode mode);

    void init(World world);

    void onCountDown();

    void onGameStart();

    void onGameEnd(IGameTeam winner);

    void onGameRestart();

    void onGamePlayerJoin(IGamePlayer gamePlayer);

    void onGamePlayerQuit(IGamePlayer gamePlayer, boolean silent);

    void onGamePlayerJoinSpectator(IGamePlayer gamePlayer);

    void onGamePlayerDeath(IGamePlayer killer, IGamePlayer victim);

    IGameTeam getTeamByEggLocation(Location location);

}
