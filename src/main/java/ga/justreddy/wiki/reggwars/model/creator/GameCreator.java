package ga.justreddy.wiki.reggwars.model.creator;

import com.avaje.ebeaninternal.server.type.EnumToDbStringMap;
import com.grinderwolf.swm.api.exceptions.InvalidWorldException;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.exceptions.WorldLoadedException;
import com.grinderwolf.swm.api.exceptions.WorldTooBigException;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.WorldManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class GameCreator implements Listener {

    private static GameCreator creator;

    public static GameCreator getCreator() {
        return creator == null ? creator = new GameCreator() : creator;
    }

    private final Map<UUID, String> stringMap;

    public Map<UUID, String> getStringMap() {
        return stringMap;
    }

    private GameCreator() {
        Bukkit.getPluginManager().registerEvents(this, REggWars.getInstance());
        this.stringMap = new HashMap<>();
    }

    @SneakyThrows
    public void create(IGamePlayer player, String name) {
        UUID uuid = player.getUniqueId();
        if (stringMap.containsKey(uuid)) return; // TODO
        File file = getFile(name);
        if (file.exists()) {
            return; // TODO
        }
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        game.set("options.team-size", 1);
        game.set("options.minimum", 8);
        game.set("options.enabled", false);
        game.save(file);

        World world = WorldManager.getManager().createNewWorld(name);
        world.getSpawnLocation().getBlock().setType(Material.STONE);
        player.teleport(world.getSpawnLocation().add(0.0, 1, 0.0));
        stringMap.put(uuid, world.getName());
        // TODO add items
    }


    @SneakyThrows
    public void save(IGamePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!stringMap.containsKey(uuid)) return; // TODO
        File file = getFile(stringMap.get(uuid));
        if (!file.exists()) return; // TODO
        FileConfiguration game = YamlConfiguration.loadConfiguration(file);
        if (!isSetupComplete(game)) {
            // TODO send message
            return;
        }
        World world = Bukkit.getServer().getWorld(stringMap.get(uuid));
        for (Player p : world.getPlayers()) {
            // TODO send to lobby
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
                    throw new RuntimeException(e);
                }
                Bukkit.getScheduler().runTask(REggWars.getInstance(), () -> {
                    WorldManager.getManager().removeWorld(world);
                    WorldManager.getManager().copySlimeWorld(world.getName());
                    GameManager.getManager().register(stringMap.get(uuid), game);
                    player.sendLegacyMessage("saved");
                    stringMap.remove(uuid);
                    // TODO
                });
            });
        } else {
            WorldManager.getManager().copyWorld(world);
            WorldManager.getManager().removeWorld(world);
            GameManager.getManager().register(stringMap.get(uuid), game);
            player.sendLegacyMessage("saved");
            stringMap.remove(uuid);
        }
    }

    public void check(IGamePlayer gamePlayer) {

    }

    @SneakyThrows
    public boolean isSetupComplete(FileConfiguration config) {
        return config.isSet("settings.displayName")
                && config.isSet("settings.minPlayers")
                && config.isSet("settings.teamSize")
                && config.isSet("teams")
                && config.isSet("generators")
                && config.isSet("waiting-lobby")
                && config.isSet("spectator-location")
                && config.isSet("bounds.lobby.high")
                && config.isSet("bounds.lobby.low")
                && config.isSet("bounds.game.high")
                && config.isSet("bounds.game.low");
    }


    public File getFile(String name) {
        return new File(REggWars.getInstance().getDataFolder().getAbsolutePath() + "/data/games/" + name + ".yml");
    }
}
