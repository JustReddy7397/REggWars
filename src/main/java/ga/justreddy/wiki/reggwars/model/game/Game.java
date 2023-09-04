package ga.justreddy.wiki.reggwars.model.game;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.*;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.ITeamAssigner;
import ga.justreddy.wiki.reggwars.model.game.team.TeamAssigner;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author JustReddy
 */
public class Game implements IGame {

    private final String name;
    private final FileConfiguration config;
    private final ResetAdapter adapter;
    private World world;
    private String displayName;
    private GameState state;
    private GameMode mode;
    private final List<IGamePlayer> players;
    private final List<IGameTeam> teams;

    private Cuboid lobbyCuboid;
    private Cuboid gameCuboid;

    private int maxPlayers;
    private int minPlayers;
    private int teamSize;

    private ITeamAssigner teamAssigner = new TeamAssigner();

    public Game(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.adapter = REggWars.getInstance().getResetAdapter();
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<IGamePlayer> getPlayers() {
        return players;
    }

    @Override
    public List<IGamePlayer> getAlivePlayers() {
        try (Stream<IGamePlayer> stream = players.stream()) {
            return stream.filter(gamePlayer -> !gamePlayer.isDead()).collect(Collectors.toList());
        }
    }

    @Override
    public List<IGamePlayer> getDeadPlayers() {
        try (Stream<IGamePlayer> stream = players.stream()) {
            return stream.filter(IGamePlayer::isDead
                    ).collect(Collectors.toList());
        }
    }

    @Override
    public int getPlayerCount() {
        return players.size();
    }

    @Override
    public List<IGameTeam> getTeams() {
        return teams;
    }

    @Override
    public List<IGameTeam> getAliveTeams() {
        try (Stream<IGameTeam> stream = teams.stream()) {
            return stream.filter(team -> team.getAlivePlayers().size() > 0)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<IGameTeam> getDeadTeams() {
        try (Stream<IGameTeam> stream = teams.stream()) {
            return stream.filter(team -> team.getAlivePlayers().size() == 0)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Region getRegion() {
        return gameCuboid;
    }

    @Override
    public int getTeamSize() {
        return teamSize;
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public void setGameState(GameState gameState) {
        state = gameState;
    }

    @Override
    public GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.mode = mode;
    }

    @Override
    public void init(World world) {
        this.world = world;
        this.displayName = config.getString("settings.displayName", name);
        this.minPlayers = config.getInt("settings.minPlayers");
        this.teamSize = config.getInt("settings.teamSize");
        this.mode = teamSize == 1 ? GameMode.SOLO : GameMode.TEAM;



        this.maxPlayers = this.teams.size() * this.teamSize;

    }

    @Override
    public void onCountDown() {

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd(IGameTeam winner) {

    }

    @Override
    public void onGameRestart() {
        if (world != null) {
            world.getPlayers().forEach(player -> {
               /* player.teleport();*/ // TODO
            });
        }
        adapter.onRestart(this);
    }

    @Override
    public void onGamePlayerJoin(IGamePlayer gamePlayer) {

    }

    @Override
    public void onGamePlayerQuit(IGamePlayer gamePlayer, boolean silent) {

    }

    @Override
    public void onGamePlayerJoinSpectator(IGamePlayer gamePlayer) {

    }

    @Override
    public void onGamePlayerDeath(IGamePlayer killer, IGamePlayer victim) {

    }

    @Override
    public IGameTeam getTeamByEggLocation(Location location) {
        return getAliveTeams().stream()
                .filter(team ->
                        LocationUtils
                                .equalsBlock(team.getEggLocation(), location))
                .findFirst()
                .orElse(null);
    }
}
