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
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShop;
import ga.justreddy.wiki.reggwars.api.model.game.shop.ShopType;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.ITeamAssigner;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.api.model.quests.QuestType;
import ga.justreddy.wiki.reggwars.board.EggWarsBoard;
import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.game.generator.Generator;
import ga.justreddy.wiki.reggwars.model.game.phase.GamePhaseManager;
import ga.justreddy.wiki.reggwars.model.game.phase.GameWaitingPhase;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import ga.justreddy.wiki.reggwars.model.game.team.TeamAssigner;
import ga.justreddy.wiki.reggwars.model.game.timer.GameEndTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.GameStartTimer;
import ga.justreddy.wiki.reggwars.model.game.timer.Timer;
import ga.justreddy.wiki.reggwars.utils.BungeeUtils;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import ga.justreddy.wiki.reggwars.utils.Util;
import ga.justreddy.wiki.reggwars.utils.player.PlayerUtil;
import lombok.Getter;
import org.bukkit.*;
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

// @Getter used for the stuff we need that is outside the IGame class
@Getter
public class Game implements IGame {

    private final String name;
    private final String server;
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
    private final List<IGamePlayer> protections;
    private final Map<UUID, Integer> kills;
    private final Map<Location, IShop> shops;
    private final List<IGamePlayer> actualPlayers;

    private Cuboid lobbyCuboid;
    private Cuboid gameCuboid;

    private Location lobbyLocation;
    private Location spectatorLocation;

    private int maxPlayers;
    private int minPlayers;
    private int teamSize;

    private Timer startTimer;
    private Timer endTimer;
    private GamePhaseManager gamePhaseManager;

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
        this.protections = new ArrayList<>();
        this.shops = new HashMap<>();
        this.actualPlayers = new ArrayList<>();
        this.server = REggWars.getInstance().getServerName();
        this.gamePhaseManager = new GamePhaseManager(this);
    }

    @Override
    public String getName() {
        return name;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public String getServer() {
        return server;
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
            return stream.filter(team -> team.getAlivePlayers().isEmpty())
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
        protections.clear();
        shops.clear();
        actualPlayers.clear();
        gamePhaseManager.setPhase(new GameWaitingPhase());
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

        ConfigurationSection shops = config.getConfigurationSection("shops");
        for (String key : shops.getKeys(false)) {
            String type = shops.getString(key + ".type");
            IShop shop = ShopManager.getManager().getShopByType(ShopType.getById(type.toUpperCase()));
            if (shop == null) continue;
            this.shops.put(LocationUtils.getLocation(shops.getString(key + ".location")), shop);
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
        this.startTimer = new GameStartTimer(20, REggWars.getInstance());
        this.endTimer = new GameEndTimer(5, REggWars.getInstance()); // TODO make choose-able time
        setGameState(GameState.WAITING); // TODO make it so if disabled, it wont enable again
        Util.updateGame(this);
    }

    @Override
    public void onCountDown() {
        gamePhaseManager.getCurrentPhase().onTick(this);
    }

    @Override
    public void onGamePlayerJoin(IGamePlayer gamePlayer) {

        System.out.println("Executor");

        EggWarsGameJoinEvent event = new EggWarsGameJoinEvent(this, gamePlayer);
        event.call();
        Player p = gamePlayer.getPlayer();

        p.setAllowFlight(false);
        p.setFlying(false);

        players.add(gamePlayer);
        actualPlayers.add(gamePlayer);
        gamePlayer.setGame(this);
        kills.put(gamePlayer.getUniqueId(), 0);
        p.getInventory().setHeldItemSlot(4);
        p.setGameMode(org.bukkit.GameMode.ADVENTURE);

        if (lobbyLocation != null && lobbyCuboid != null) {
            gamePlayer.teleport(lobbyLocation);
        }

        PlayerUtil.refresh(gamePlayer.getPlayer());

        // TODO set possibly their rank
        EggWarsBoard.getManager().removeScoreboard(gamePlayer);
        PlayerUtil.clearInventory(gamePlayer.getPlayer());
        //HotBarManager.getManager().giveItems(player.getPlayer(), HotBarType.GAME);
        for (Player player1 : world.getPlayers()) {
            player1.showPlayer(gamePlayer.getPlayer());
            gamePlayer.getPlayer().showPlayer(player1);
        }
        // TODO create scoreboard
        EggWarsBoard.getManager().setGameBoard(gamePlayer);
        System.out.println("Board set!");

        Util.updateGame(this);
    }

    @Override
    public void onGamePlayerQuit(IGamePlayer gamePlayer, boolean silent, boolean local) {
        spectators.remove(gamePlayer);
        players.remove(gamePlayer);
        actualPlayers.remove(gamePlayer);
        gamePlayer.setGame(null);
        gamePlayer.setDead(false);
        IGameTeam team = gamePlayer.getTeam();
        if (team != null) {
            team.removePlayer(gamePlayer);
        }

        gamePlayer.setTeam(null);

        EggWarsBoard.getManager().removeScoreboard(gamePlayer);
        // TODO teleport to lobby
        PlayerUtil.refresh(gamePlayer.getPlayer());

        if (Core.MODE == ServerMode.BUNGEE) {
            if (local) return;
            BungeeUtils.getInstance().sendBackToServer(gamePlayer);
        } else {
            gamePlayer.teleport(REggWars.getInstance().getSpawnLocation());
        }

        EggWarsGameLeaveEvent event = new EggWarsGameLeaveEvent(this, gamePlayer);
        event.call();
        Util.updateGame(this);
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
    public void onGamePlayerJoinSpectator(IGamePlayer gamePlayer, boolean spectating) {

    }

    @Override
    public void onGamePlayerDeath(IGamePlayer killer, IGamePlayer victim, String path, boolean isFinal) {
        if (victim.isDead()) return;
        EggWarsGameKillEvent event = new EggWarsGameKillEvent(this, killer, victim, isFinal);
        event.call();

        if (victim.getTeam().isEggGone()) {
            victim.setDead(true);
        } else {
            victim.setFakeDead(true);
        }

        Player player = victim.getPlayer();
        PlayerUtil.refresh(player);
        Bukkit.getScheduler().runTaskLater(REggWars.getInstance(), () -> {
            player.teleport(spectatorLocation);
            player.setGameMode(org.bukkit.GameMode.ADVENTURE);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 4));

        }, 3L);
        for (IGamePlayer players : players) {
            players.getPlayer().hidePlayer(player);
        }

        for (IGamePlayer players : getDeadPlayers()) {
            players.getPlayer().hidePlayer(player);
        }

        if (killer != null) {
            KillMessage messages = KillMessageManager
                    .getManager().getById(0);
            if (path != null && !path.isEmpty()) {
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

        if (!isFinal) {
            new BukkitRunnable() {
                int i = 5;

                @Override
                public void run() {
                    if (!players.contains(victim)) {
                        cancel();
                        return;
                    }
                    if (i <= 0) {
                        victim.setDead(false);
                        victim.setFakeDead(false);
                        IGameTeam team = victim.getTeam();
                        PlayerUtil.refresh(player);
                        for (IGamePlayer players : players) {
                            players.getPlayer().showPlayer(player);
                        }

                        for (IGamePlayer players : getDeadPlayers()) {
                            players.getPlayer().showPlayer(player);
                        }
                        victim.teleport(team.getSpawnLocation());
                        victim.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
                        addRespawnProtection(victim);
                        Bukkit.getScheduler().runTaskLater(REggWars.getInstance(), () -> removeRespawnProtection(victim),
                                REggWars.getInstance().getSettingsConfig()
                                        .getConfig().getInt("game.respawn-protection") * 20L
                        );
                        // TODO send respawned title and message
                        cancel();
                        return;
                    }

                    victim.sendTitle(Message.TITLES_RESPAWNING_TITLE, Message.TITLES_RESPAWNING_SUBTITLE,
                            new Replaceable("<time>", String.valueOf(i))
                    );
                    victim.sendMessage(Message.MESSAGES_GAME_RESPAWN, new Replaceable("<time>", String.valueOf(i)));

                    --i;
                }
            }.runTaskTimer(REggWars.getInstance(), 7L, 20L);
        } else {
            victim.sendTitle(Message.TITLES_DIED_TITLE, Message.TITLES_DIED_SUBTITLE
            );
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
    public IGameSign getGeneratorSignByLocation(Location location) {
        IGenerator generator = generators.stream().filter(
                gen -> LocationUtils.equalsBlock(gen.getGameSign()
                                .getLocation().getBlock().getLocation(),
                        location.getBlock().getLocation())
        ).findFirst().orElse(null);
        if (generator == null) return null;
        return generator.getGameSign();
    }

    @Override
    public IShop getShopByLocation(Location location) {
        for (Map.Entry<Location, IShop> entry : shops.entrySet()) {
            if (LocationUtils.equals(entry.getKey(), location)) {
                return entry.getValue();
            }
        }
        return null;
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
    public void addRespawnProtection(IGamePlayer gamePlayer) {
        protections.add(gamePlayer);
    }

    @Override
    public boolean hasRespawnProtection(IGamePlayer gamePlayer) {
        return protections.contains(gamePlayer);
    }

    @Override
    public void removeRespawnProtection(IGamePlayer gamePlayer) {
        protections.remove(gamePlayer);
    }

    @Override
    public void goToNextPhase() {
        gamePhaseManager.nextPhase();
    }

    @Override
    public World getWorld() {
        return world;
    }

    public void toSpawn(IGameTeam team) {
        team.getAlivePlayers().forEach(player -> player.teleport(team.getSpawnLocation()));
    }


}
