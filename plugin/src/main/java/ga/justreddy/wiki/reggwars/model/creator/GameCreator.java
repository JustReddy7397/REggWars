package ga.justreddy.wiki.reggwars.model.creator;

import com.grinderwolf.swm.api.exceptions.InvalidWorldException;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.exceptions.WorldLoadedException;
import com.grinderwolf.swm.api.exceptions.WorldTooBigException;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.team.Team;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.WorldManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author JustReddy
 */
@Getter
public class GameCreator implements Listener {

    private static GameCreator creator;

    public static GameCreator getCreator() {
        return creator == null ? creator = new GameCreator() : creator;
    }

    private final Map<UUID, String> stringMap;
    private final Map<UUID, FileConfiguration> generatorSetup;

    private GameCreator() {
        Bukkit.getPluginManager().registerEvents(this, REggWars.getInstance());
        this.stringMap = new HashMap<>();
        this.generatorSetup = new HashMap<>();
    }

    @SneakyThrows
    public void create(IGamePlayer player, String name) {
        UUID uuid = player.getUniqueId();
        if (stringMap.containsKey(uuid)) return; // TODO
        File file = getFile(name);
        if (file.exists()) {
            player.sendMessage(Message.MESSAGES_ARENA_ALREADY_EXISTS);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        game.set("settings.team-size", 1);
        game.set("settings.minPlayers", 8);
        game.set("settings.enabled", false);
        game.save(file);

        World world = WorldManager.getManager().createNewWorld(name);
        world.getSpawnLocation().getBlock().setType(Material.STONE);
        player.teleport(world.getSpawnLocation().add(0.0, 1, 0.0));
        stringMap.put(uuid, world.getName());
        player.sendMessage(Message.MESSAGES_ARENA_CREATED);
        // TODO add items
    }

    @SneakyThrows
    public void save(IGamePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        if (!isSetupComplete(game)) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_COMPLETED);
            return;
        }
        World world = Bukkit.getServer().getWorld(stringMap.get(uuid));
        for (Player p : world.getPlayers()) {
            if (REggWars.getInstance().getSpawnLocation() == null) break;
            p.teleport(REggWars.getInstance().getSpawnLocation());
        }

        world.save();
        Bukkit.unloadWorld(world.getName(), true);
        if (REggWars.getInstance().isSlimeEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
                try {
                    REggWars.getInstance().getSlime().importWorld(
                            world.getWorldFolder(),
                            world.getName(),
                            REggWars.getInstance().getLoader()
                    );
                } catch (WorldAlreadyExistsException |
                         InvalidWorldException |
                         WorldLoadedException |
                         WorldTooBigException |
                         IOException e) {
                    stringMap.remove(uuid);
                    throw new RuntimeException(e);
                }
                Bukkit.getScheduler().runTask(REggWars.getInstance(), () -> {
                    WorldManager.getManager().removeWorld(world);
                    WorldManager.getManager().copySlimeWorld(world.getName());
                    GameManager.getManager().register(stringMap.get(uuid), game);
                    player.sendMessage(Message.MESSAGES_ARENA_SAVED,
                            new Replaceable("<name>", stringMap.get(uuid)));
                });
            });
        } else {
            WorldManager.getManager().copyWorld(world);
            WorldManager.getManager().removeWorld(world);
            GameManager.getManager().register(stringMap.get(uuid), game);
            player.sendMessage(Message.MESSAGES_ARENA_SAVED,
                    new Replaceable("<name>", stringMap.get(uuid)));
        }
        stringMap.remove(uuid);
    }

    @SneakyThrows
    public void addTeam(IGamePlayer gamePlayer, Team team) {
        UUID uuid = gamePlayer.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        if (doesTeamExist(game, team)) return; // TODO
        game.set("teams." + team.getIdentifier() + ".egg", "");
        game.set("teams." + team.getIdentifier() + ".spawn", "");
        game.save(file);
        gamePlayer.sendMessage(Message.MESSAGES_ARENA_TEAM_ADDED,
                new Replaceable("<team>", team.getDisplayName()));
    }

    @SneakyThrows
    public void setEgg(IGamePlayer gamePlayer, Team team) {
        UUID uuid = gamePlayer.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        if (!doesTeamExist(game, team)) return; // TODO
        game.set("teams." + team.getIdentifier() + ".egg", LocationUtils.toLocation(gamePlayer.getLocation().getBlock().getLocation()));
        game.save(file);
        gamePlayer.sendMessage(Message.MESSAGES_ARENA_TEAM_SET_EGG,
                new Replaceable("<team>", team.getDisplayName()));
    }

    // TODO EDIT SO RESPAWN AND CAGE SPAWN ARE DIFFERENT
    @SneakyThrows
    public void setSpawn(IGamePlayer gamePlayer, Team team) {
        UUID uuid = gamePlayer.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        if (!doesTeamExist(game, team)) return; // TODO
        game.set("teams." + team.getIdentifier() + ".spawn", LocationUtils.toLocation(gamePlayer.getLocation().getBlock().getLocation()));
        game.save(file);
        gamePlayer.sendMessage(Message.MESSAGES_ARENA_TEAM_SET_SPAWN,
                new Replaceable("<team>", team.getDisplayName()));
    }

    @SneakyThrows
    public void setWaitingLobby(IGamePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        game.set("waiting-lobby", LocationUtils.toLocation(player.getLocation()));
        game.save(file);
        player.sendMessage(Message.MESSAGES_ARENA_WAITING_LOBBY_SET);
    }

    @SneakyThrows
    public void setSpectatorSpawn(IGamePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(uuid)) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_CREATING);
            return;
        }
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) {
            player.sendMessage(Message.MESSAGES_ARENA_NOT_FOUND);
            return;
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        game.set("spectator-location", LocationUtils.toLocation(player.getLocation()));
        game.save(file);
        player.sendMessage(Message.MESSAGES_ARENA_SPECTATOR_SET);
    }

    public void check(IGamePlayer gamePlayer) {
    }

    public boolean isTeamSetup(FileConfiguration config, Team team) {
        return config.isSet("teams." + team.getIdentifier() + ".spawn") &&
                !config.getString("teams." + team.getIdentifier() + ".spawn", "")
                        .equalsIgnoreCase("")
                && config.isSet("teams." + team.getIdentifier() + ".egg")
                && !config.getString("teams." + team.getIdentifier() + ".egg", "")
                .equalsIgnoreCase("")
                ;
    }

    @SneakyThrows
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(player.getUniqueId())) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        String name = stringMap.get(uuid);
        File file = getFile(name);
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String location;
        if (itemStack.getType() == Material.STICK) {
            location = LocationUtils.toLocation(event.getClickedBlock().getLocation());
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    config.set("bounds.lobby.high", location);
                    config.save(file);
                    gamePlayer.sendMessage(Message.MESSAGES_ARENA_BOUND_LOBBY_HIGH);
                    event.setCancelled(true);
                    break;
                case RIGHT_CLICK_BLOCK:
                    config.set("bounds.lobby.low", location);
                    config.save(file);
                    gamePlayer.sendMessage(Message.MESSAGES_ARENA_BOUND_LOBBY_LOW);
                    event.setCancelled(true);
                    break;
                // TODO
            }
        } else if (itemStack.getType() == Material.BLAZE_ROD) {
            location = LocationUtils.toLocation(event.getClickedBlock().getLocation());
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    config.set("bounds.game.high", location);
                    config.save(file);
                    gamePlayer.sendMessage(Message.MESSAGES_ARENA_BOUND_ARENA_HIGH);
                    event.setCancelled(true);
                    break;
                case RIGHT_CLICK_BLOCK:
                    config.set("bounds.game.low", location);
                    config.save(file);
                    gamePlayer.sendMessage(Message.MESSAGES_ARENA_BOUND_ARENA_LOW);
                    event.setCancelled(true);
                    break;
                // TODO
            }

        }
    }

    @SneakyThrows
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (!stringMap.containsKey(player.getUniqueId())) return;
        if (event.getLines().length < 4) return;
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        String first = event.getLine(0);
        String second = event.getLine(1);
        String third = event.getLine(2);
        String fourth = event.getLine(3);
        if (first.equalsIgnoreCase("[GENERATOR]")) {
            FileConfiguration config = REggWars.getInstance().getGeneratorsConfig().getConfig();
            GeneratorType type = GeneratorType.getType(second.toUpperCase());
            if (type == null) {
                event.getBlock().setType(Material.AIR);
                gamePlayer.sendMessage(Message.MESSAGES_ARENA_GENERATOR_WRONG_TYPE);
                return;
            }

            int startlevel;

            try {
                startlevel = Integer.parseInt(third);
            } catch (NumberFormatException e) {
                event.getBlock().setType(Material.AIR);
                gamePlayer.sendMessage(Message.MESSAGES_ARENA_INVALID_GENERATOR_LEVEL,
                        new Replaceable("<level>",
                                config.getConfigurationSection("spawners."
                                                + type.name().toLowerCase())
                                        .getKeys(false).size() + ""
                        )
                );
                return;
            }

            File file = getFile(stringMap.get(player.getUniqueId()));
            FileConfiguration c = YamlConfiguration.loadConfiguration(file);
            int current = getGenerators(c);
            c.set("generators." + current + ".startLevel", startlevel);
            c.set("generators." + current + ".maxLevel",
                    (config.getConfigurationSection("spawners." + type.name()
                            .toLowerCase()).getKeys(false).size() - 1));
            c.set("generators." + current + ".material", getMaterial(type.name()).name());
            c.set("generators." + current + ".location", LocationUtils.toLocation(event.getBlock().getLocation()));
            c.set("generators." + current + ".type", type.name());
            c.save(file);
            gamePlayer.sendMessage(Message.MESSAGES_ARENA_GENERATOR_ADDED);
        }

    }

    @SneakyThrows
    public void setMinPlayers(IGamePlayer player, int players) {
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(uuid)) return; // TODO
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) return; // TODO
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        game.set("settings.minPlayers", players);
        game.save(file);
        // TODO send proper message
        player.sendLegacyMessage("Set waiting-lobby");
    }

    private int getGenerators(FileConfiguration config) {
        if (!config.isSet("generators.")) {
            return 1;
        }

        int count = 1;

        Set<String> generators = config.getConfigurationSection("generators").getKeys(false);
        for (String key : generators) {
            ConfigurationSection section = config.getConfigurationSection("generators." + key);
            if (section.isSet("startLevel") && section.isSet("maxLevel")
                    && section.isSet("material") && section.isSet("location")
                    && section.isSet("type")) {
                ++count;
            }
        }
        return count;
    }

    @SneakyThrows
    public boolean isSetupComplete(FileConfiguration config) {
        return config.isSet("settings.displayName")
                && config.isSet("settings.minPlayers")
                && config.isSet("settings.team-size")
                && config.isSet("teams.")
                && config.isSet("generators.")
                && config.isSet("waiting-lobby")
                && config.isSet("spectator-location")
                && config.isSet("bounds.lobby.high")
                && config.isSet("bounds.lobby.low")
                && config.isSet("bounds.game.high")
                && config.isSet("bounds.game.low");
    }

    public boolean doesTeamExist(FileConfiguration config, Team team) {
        return config.isSet("teams." + team.getIdentifier());
    }

    public boolean hasMoreThatOneTeam(FileConfiguration config) {
        return config.getConfigurationSection("teams").getKeys(false).size() > 1;
    }

    @SneakyThrows
    public boolean areTeamsComplete(FileConfiguration configuration) {
        ConfigurationSection section = configuration.getConfigurationSection("teams");
        for (String key : section.getKeys(false)) {
            ConfigurationSection team = section.getConfigurationSection(key);
            if (!team.isSet("egg")
                    && team.getString("egg", "")
                    .equalsIgnoreCase("")
                    && !team.isSet("spawn")
                    && team.getString("spawn", "")
                    .equalsIgnoreCase("")
            ) return false;
        }

        return true;
    }

    public File getFile(String name) {
        return new File(REggWars.getInstance().getDataFolder().getAbsolutePath() + "/data/games/" + name + ".yml");
    }

    private Material getMaterial(String input) {
        switch (input) {
            case "IRON":
                return Material.IRON_INGOT;
            case "GOLD":
                return Material.GOLD_INGOT;
            case "DIAMOND":
                return Material.DIAMOND;
            case "EMERALD":
                return Material.EMERALD;
        }
        return null;
    }


}
