package ga.justreddy.wiki.reggwars.model.game;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.*;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.ITeamAssigner;
import ga.justreddy.wiki.reggwars.model.game.generator.Generator;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import ga.justreddy.wiki.reggwars.model.game.team.TeamAssigner;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
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
    private final List<IGenerator> generators;
    private final List<Region> regions;

    private Cuboid lobbyCuboid;
    private Cuboid gameCuboid;

    private Location lobbyLocation;
    private Location spectatorLocation;

    private int maxPlayers;
    private int minPlayers;
    private int teamSize;

    private final ITeamAssigner teamAssigner = new TeamAssigner();

    public Game(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.adapter = REggWars.getInstance().getResetAdapter();
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.generators = new ArrayList<>();
        this.regions = new ArrayList<>();
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
    public Region getGameRegion() {
        return gameCuboid;
    }

    @Override
    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public void addRegion(Region region) {
        regions.add(region);
    }

    @Override
    public void removeRegion(Region region) {
        regions.remove(region);
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
        players.clear();
        teams.clear();
        generators.clear();
        regions.clear();
        this.displayName = config.getString("settings.displayName", name);
        this.minPlayers = config.getInt("settings.minPlayers");
        this.teamSize = config.getInt("settings.teamSize");
        this.mode = teamSize == 1 ? GameMode.SOLO : GameMode.TEAM;
        ConfigurationSection teams = config.getConfigurationSection("teams");
        for (String id : teams.getKeys(false)) {
            ConfigurationSection section = teams.getConfigurationSection(id);
            IGameTeam team = new GameTeam(id, section);
            this.teams.add(team);
        }

        ConfigurationSection generators = config.getConfigurationSection("generators");

        for (String key : generators.getKeys(false)) {
            ConfigurationSection section = generators.getConfigurationSection(key);
            IGenerator generator = new Generator(
                    key,
                    section.getInt("startLevel"),
                    section.getInt("maxLevel"),
                    Material.matchMaterial(section.getString("material")),
                    LocationUtils.getLocation(section.getString("location")),
                    this,
                    GeneratorType.valueOf(section.getString("type").toUpperCase())
            );
            this.generators.add(generator);
        }

        this.maxPlayers = this.teams.size() * this.teamSize;
        this.lobbyLocation = LocationUtils.getLocation("waiting-lobby");
        this.spectatorLocation = LocationUtils.getLocation("spectator-location");
        Location high;
        Location low;
        if (lobbyLocation != null) {
            high = LocationUtils.getLocation("bounds.lobby.high");
            low = LocationUtils.getLocation("bounds.lobby.low");
            lobbyCuboid = new Cuboid(high, low, true);
        }

        high = LocationUtils.getLocation("bounds.game.high");
        low = LocationUtils.getLocation("bounds.game.low");
        gameCuboid = new Cuboid(high, low, false);

    }

    @Override
    public void onCountDown() {
        switch (state) {
            // TODO
        }
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
