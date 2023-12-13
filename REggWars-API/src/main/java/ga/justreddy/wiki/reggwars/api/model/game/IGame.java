package ga.justreddy.wiki.reggwars.api.model.game;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShop;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JustReddy
 */
public interface IGame {

    String getName();

    String getServer();

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

    boolean isGameState(GameState state);

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

    void onGamePlayerJoinSpectator(IGamePlayer gamePlayer, boolean spectating);

    void onGamePlayerDeath(IGamePlayer killer, IGamePlayer victim, String path, boolean isFinal);

    Set<IGameTeam> getTeamsSet();

    List<Map.Entry<IGamePlayer, Integer>> getTopKillers();

    int getKills(IGamePlayer player);

    void addKill(IGamePlayer player);

    List<IGameTeam> getTeamsOrdered();

    IGameTeam getTeamByEggLocation(Location location);

    IGenerator getGeneratorByLocation(Location location);

    IGameSign getGeneratorSignByLocation(Location location);

    IShop getShopByLocation(Location location);

    int getMaxPlayers();

    void sendMessage(Message message);

    void sendLegacyMessage(String message);

    void sendMessage(Message message, Replaceable... replaceable);

    void sendListMessage(Message message);

    void sendListMessage(Message message, Replaceable... replaceable);

    void sendTitle(Message title, Message subTitle);

    void sendTitle(Message title, Message subTitle, Replaceable... replaceable);

    void sendActionBar(Message message);

    void sendActionBar(Message message, Replaceable... replaceable);

    void sendSound(String sound);

    void addBlock(Location location);

    boolean isPlacedBlock(Location location);

    void removeBlock(Location location);

    void addRespawnProtection(IGamePlayer gamePlayer);

    boolean hasRespawnProtection(IGamePlayer gamePlayer);

    void removeRespawnProtection(IGamePlayer gamePlayer);

    World getWorld();

    default List<String> getPlayerNames() {
        return getPlayers().stream()
                .filter(players -> !players.isSpectating())
                .map(IGamePlayer::getName)
                .collect(Collectors.toList());
    }

}
