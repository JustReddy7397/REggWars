package ga.justreddy.wiki.reggwars.model.game;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameJoinEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameKillEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameLeaveEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameStartEvent;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.*;
import ga.justreddy.wiki.reggwars.api.model.game.GameMode;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.ITeamAssigner;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.board.EggWarsBoard;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.game.generator.Generator;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import ga.justreddy.wiki.reggwars.model.game.team.TeamAssigner;
import ga.justreddy.wiki.reggwars.model.game.timer.GameEndTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.GameStartTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.GameTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.Timer;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import ga.justreddy.wiki.reggwars.utils.player.PlayerUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
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
    private final List<IGamePlayer> spectators;
    private final List<IGameTeam> teams;
    private final List<IGenerator> generators;
    private final List<Region> regions;
    private final List<Location> placedBlocks;

    private final Map<UUID, Integer> kills;

    private Cuboid lobbyCuboid;
    private Cuboid gameCuboid;

    private Location lobbyLocation;
    private Location spectatorLocation;

    private int maxPlayers;
    private int minPlayers;
    private int teamSize;

    private Timer gameTimer;
    private Timer startTimer;
    private Timer endTimer;

    private final ITeamAssigner teamAssigner = new TeamAssigner();

    public Game(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.adapter = REggWars.getInstance().getResetAdapter();
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.generators = new ArrayList<>();
        this.regions = new ArrayList<>();
        this.kills = new HashMap<>();
        this.spectators = new ArrayList<>();
        this.placedBlocks = new ArrayList<>();
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
            return stream.filter(team -> !team.getAlivePlayers().isEmpty() && !team.isEggGone())
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
    public boolean isGameState(GameState state) {
        return this.state == state;
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
        kills.clear();
        spectators.clear();
        setGameState(GameState.WAITING);
        this.displayName = config.getString("settings.displayName", name);
        this.minPlayers = config.getInt("settings.minPlayers");
        this.teamSize = config.getInt("settings.team-size");
        this.mode = teamSize == 1 ? GameMode.SOLO : GameMode.TEAM;
        ConfigurationSection teams = config.getConfigurationSection("teams");
        for (String id : teams.getKeys(false)) {
            ConfigurationSection section = teams.getConfigurationSection(id);
            IGameTeam team = new GameTeam(id, this, section);
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

        this.lobbyLocation = LocationUtils.getLocation(config.getString("waiting-lobby"));
        this.spectatorLocation = LocationUtils.getLocation(config.getString("spectator-location"));
        Location high;
        Location low;
        if (lobbyLocation != null) {
            high = LocationUtils.getLocation(config.getString("bounds.lobby.high"));
            low = LocationUtils.getLocation(config.getString("bounds.lobby.low"));
            lobbyCuboid = new Cuboid(high, low, true);
        }

        high = LocationUtils.getLocation(config.getString("bounds.game.high"));
        low = LocationUtils.getLocation(config.getString("bounds.game.low"));
        gameCuboid = new Cuboid(high, low, false);
        this.gameTimer = new GameTimer(0, REggWars.getInstance(), this);
        this.startTimer = new GameStartTimer(10, REggWars.getInstance());
        endTimer = new GameEndTimer(5, REggWars.getInstance()); // TODO make choose-able time
    }

    @Override
    public void onCountDown() {
        switch (state) {
            // TODO
            case STARTING:

                if (getPlayerCount() < minPlayers) {
                    setGameState(GameState.WAITING);
                    gameTimer.stop();
                    startTimer.stop();
                    return;
                }
                if (startTimer.getTicksExceed() <= 0) {
                    onGameStart();
                    startTimer.stop();
                    return;
                }
                break;
            case PLAYING:
/*                if (getAliveTeams().size() == 1) {
                    onGameEnd(getAliveTeams().get(0));
                } else if (getAliveTeams().isEmpty()) {
                    onGameEnd(null);
                }*/

                generators.forEach(generator -> {
                    generator.getGameSign().update();
                });


                break;
            case ENDING:
                break;
        }
    }

    @Override
    public void onGameStart() {
        setGameState(GameState.PLAYING);
        if (!gameTimer.isStarted()) gameTimer.start();
        teamAssigner.assignTeam(this);
        Bukkit.getScheduler().runTaskLater(REggWars.getInstance(), () -> {
            EggWarsGameStartEvent event = new EggWarsGameStartEvent(this);
            event.call();
            generators.forEach(IGenerator::start);
            teams.forEach(team -> {
                toSpawn(team);
                team.getEggLocation().getBlock().setType(Material.DRAGON_EGG);
                REggWars.getInstance().getNms().setTeamName(team);
            });
            if (lobbyLocation != null && lobbyCuboid != null) {
                lobbyCuboid.clear();
            }

            for (IGamePlayer gamePlayer : getAlivePlayers()) {
                Player player = gamePlayer.getPlayer();
                player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                PlayerUtil.clearInventory(player);

            }


        }, 5);
    }

    @Override
    public void onGameEnd(IGameTeam winner) {
        generators.forEach(IGenerator::destroy);
        endTimer.start();
        setGameState(GameState.ENDING);
        sendLegacyMessage("Thanks for playing bitches");
    }

    @Override
    public void onGameRestart() {
        if (world != null) {
            world.getPlayers().forEach(player -> {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            });
        }
        adapter.onRestart(this);
    }


    @Override
    public void onGamePlayerJoin(IGamePlayer gamePlayer) {

        EggWarsGameJoinEvent event = new EggWarsGameJoinEvent(this, gamePlayer);
        event.call();
        Player p = gamePlayer.getPlayer();

        p.setAllowFlight(false);
        p.setFlying(false);

        if (isGameState(GameState.DISABLED)) {
            // TODO sendm essage
            return;
        }

        if (isGameState(GameState.PLAYING) || isGameState(GameState.ENDING)) {
            // tODO send message
            return;
        }

        if (players.size() > maxPlayers) {
            // TODO sendm esage
            return;
        }

        if (players.contains(gamePlayer)) return;

        players.add(gamePlayer);
        gamePlayer.setGame(this);
        kills.put(gamePlayer.getUniqueId(), 0);
        p.getInventory().setHeldItemSlot(4);
        p.setGameMode(org.bukkit.GameMode.ADVENTURE);

        if (lobbyLocation != null && lobbyCuboid != null) {
            gamePlayer.teleport(lobbyLocation);
        }

        PlayerUtil.refresh(gamePlayer.getPlayer());

        // TODO set possible their rank

        REggWars.getInstance().getServer().getScheduler().runTaskLater(REggWars.getInstance(), () -> {
/*            EggWarsBoard.getScoreboard().removeScoreboard(player);
            PlayerUtil.clearInventory(player.getPlayer(), getClass());
            SkyWarsBoard.getScoreboard().setGameBoard(player);
            HotBarManager.getManager().giveItems(player.getPlayer(), HotBarType.GAME);
            for (Player player1 : gameMap.getWorld().getPlayers()) {
                player1.showPlayer(player.getPlayer());
                player.getPlayer().showPlayer(player1);
            }*/
            // TODO create scoreboard
            EggWarsBoard.getManager().setGameBoard(gamePlayer);
        }, 10L);


        if (players.size() > minPlayers && isGameState(GameState.WAITING)) {
            setGameState(GameState.STARTING);
            if (!gameTimer.isStarted()) gameTimer.start();
            startTimer.start();
        }


    }

    @Override
    public void onGamePlayerQuit(IGamePlayer gamePlayer, boolean silent) {
        spectators.remove(gamePlayer);
        players.remove(gamePlayer);
        gamePlayer.setGame(null);
        gamePlayer.setDead(false);
        IGameTeam team = gamePlayer.getTeam();
        if (team != null) {
            team.removePlayer(gamePlayer);
        }

        gamePlayer.setTeam(null);

        // TODO remove scoreboard
        // TODO teleport to lobby
        PlayerUtil.refresh(gamePlayer.getPlayer());
        EggWarsGameLeaveEvent event = new EggWarsGameLeaveEvent(this, gamePlayer);
        event.call();
        if (gamePlayer.isDead()) return;

        if (silent) return;
        // TODO make sendMessage method and stuff
/*        sendMessage(Messages.MESSAGES_GAME_LEAVE,
                new Replaceable("<player>", gamePlayer.getName()),
                new Replaceable("<count>", String.valueOf(getPlayerCount())),
                new Replaceable("<max>", String.valueOf(maxPlayers))
        );*/


    }

    @Override
    public void onGamePlayerJoinSpectator(IGamePlayer gamePlayer) {

    }

    @Override
    public void onGamePlayerDeath(IGamePlayer killer, IGamePlayer victim, String path, boolean isFinal) {
        if (victim.isDead()) return;
        EggWarsGameKillEvent event = new EggWarsGameKillEvent(this, killer, victim, isFinal);
        event.call();

        victim.setDead(true);
        Player player = victim.getPlayer();
        Bukkit.getServer().getScheduler().runTaskLater(REggWars.getInstance(), () -> {
            if (player == null) return;
            player.setGameMode(org.bukkit.GameMode.ADVENTURE);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 4));
            player.teleport(spectatorLocation);
            if (isFinal) {
                //HotBarManager.getManager().giveItems(player, HotBarType.SPECTATOR);
            }

        }, 5L);
        for (IGamePlayer players : players) {
            players.getPlayer().hidePlayer(player);
        }

        for (IGamePlayer players : getDeadPlayers()) {
            players.getPlayer().hidePlayer(player);
        }

        if (killer != null) {
            KillMessage messages = KillMessageManager
                    .getManager().getById(killer.getCosmetics().getSelectedKillMessage());
            if (path != null || !path.isEmpty()) {
                switch (path) {
                    case "void":
                        messages.sendVoidMessage(this, killer, victim, isFinal);
                        break;
                    case "fall":
                        messages.sendFallMessage(this, killer, victim, isFinal);
                        break;
                    case "burned":
                    case "lava":
                    case "explosion":
                    case "drowning":
                    case "suffocation":
                    case "melee":
                    case "unknown":
                        messages.sendMeleeMessage(this, killer, victim, isFinal);
                        break;
                    case "projectile":
                        messages.sendProjectileMessage(this, killer, victim, isFinal);
                        break;

                }
            }
        }

        if (isFinal) {

        } else {
            // TODO send respawn stuff
            new BukkitRunnable() {
                int i = 5;

                @Override
                public void run() {
                    if (!players.contains(victim)) {
                        cancel();
                        return;
                    }
                    if (i <= 5) {
                        IGameTeam team = victim.getTeam();
                        victim.teleport(team.getSpawnLocation());
                        // TODO send respawned title and message
                        cancel();
                        return;
                    }

                    // TODO send respawn title and message

                    --i;
                }
            }.runTaskTimer(REggWars.getInstance(), 7L, 20L);
        }
    }

    @Override
    public Set<IGameTeam> getTeamsSet() {
        return new HashSet<>(teams);
    }

    @Override
    public List<Map.Entry<IGamePlayer, Integer>> getTopKillers() {
        return new ArrayList<>();
    }

    @Override
    public int getKills(IGamePlayer player) {
        return kills.getOrDefault(player.getUniqueId(), 0);
    }

    @Override
    public void addKill(IGamePlayer player) {
        kills.replace(player.getUniqueId(),
                getKills(player), (getKills(player) + 1));
    }

    @Override
    public List<IGameTeam> getTeamsOrdered() {
        return teams.stream().sorted(Comparator.comparingInt(team -> team.getTeam().getWeight())).collect(Collectors.toList());
    }

    @Override
    public IGameTeam getTeamByEggLocation(Location location) {
        try (Stream<IGameTeam> stream = getTeams().stream()) {
            return stream.filter(team ->
                    team.getEggLocation() != null &&
                            team.getEggLocation().getBlock().getType() == XMaterial.DRAGON_EGG.parseMaterial()
                            &&
                        LocationUtils.equalsBlock(team.getEggLocation(), location)
                    )
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public IGenerator getGeneratorByLocation(Location location) {
        try (Stream<IGenerator> stream = generators.stream()) {
            return stream.filter(generator -> LocationUtils.equalsBlock(
                            generator.getLocation(), location
                    ))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void sendMessage(Message message) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendMessage(player, message);
        });
    }

    @Override
    public void sendLegacyMessage(String message) {
        players.forEach(player -> {
            player.sendLegacyMessage(message);
        });
    }

    @Override
    public void sendMessage(Message message, Replaceable... replaceable) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendMessage(player, message, replaceable);
        });
    }

    @Override
    public void sendListMessage(Message message) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            List<String> bitches = language.getStringList(message);
            for (String str : bitches) {
                player.sendLegacyMessage(ChatUtil.format(str));
            }
        });
    }

    @Override
    public void sendListMessage(Message message, Replaceable... replaceable) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            List<String> bitches = language.getStringList(message);
            for (String str : bitches) {
                for (Replaceable rp : replaceable) {
                    str = str.replaceAll(rp.getKey(), rp.getValue());
                }
                player.sendLegacyMessage(ChatUtil.format(str));
            }
        });
    }

    @Override
    public void sendTitle(Message title, Message subTitle) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendTitle(player, title, subTitle);
        });
    }

    @Override
    public void sendTitle(Message title, Message subTitle, Replaceable... replaceable) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendTitle(player, title, subTitle, replaceable);
        });
    }

    @Override
    public void sendActionBar(Message message) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendActionBar(player, message);
        });
    }

    @Override
    public void sendActionBar(Message message, Replaceable... replaceable) {
        players.forEach(player -> {
            ILanguage language = player.getSettings().getLanguage();
            language.sendActionBar(player, message, replaceable);
        });
    }

    @Override
    public void sendSound(String sound) {
        players.forEach(player -> player.sendSound(sound));
    }

    @Override
    public void addBlock(Location location) {
        if (placedBlocks.contains(location)) return;
        placedBlocks.add(location);
    }

    @Override
    public boolean isPlacedBlock(Location location) {
        return placedBlocks.contains(location);
    }

    @Override
    public void removeBlock(Location location) {
        placedBlocks.add(location);
    }

    @Override
    public World getWorld() {
        return world;
    }

    private void toSpawn(IGameTeam team) {
        team.getAlivePlayers().forEach(player -> player.teleport(team.getSpawnLocation()));
    }


}
