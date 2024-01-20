package ga.justreddy.wiki.reggwars.manager;

import com.grinderwolf.swm.api.exceptions.*;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.nms.Nms;
import ga.justreddy.wiki.reggwars.utils.FileUtils;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

/**
 * @author JustReddy
 */
public class WorldManager {

    private static WorldManager manager;

    public static WorldManager getManager() {
        return manager == null ? manager = new WorldManager() : manager;
    }

    private final Nms nms;

    private WorldManager() {
        this.nms = REggWars.getInstance().getNms();
    }

    public World createNewWorld(String name) {
        WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.generateStructures(false);
        worldCreator.generator(nms.getGenerator());
        World world = worldCreator.createWorld();
        world.setDifficulty(Difficulty.EASY);
        world.setSpawnFlags(true, true);
        world.setPVP(true);
        world.setStorm(false);
        world.setThundering(false);
        world.setKeepSpawnInMemory(false);
        world.setAutoSave(false);
        world.setTicksPerAnimalSpawns(1);
        world.setTicksPerMonsterSpawns(1);
        world.setWeatherDuration(Integer.MAX_VALUE);
        world.setSpawnLocation(0, 30, 0);
        nms.setWorldRule(world, "doMobSpawning", false);
        nms.setWorldRule(world, "doFireTick", false);
        nms.setWorldRule(world, "showDeathMessages", false);
        nms.setWorldRule(world, "announceAdvancements", false);
        return world;
    }

    @SneakyThrows
    public void copyWorld(World world) {
        File worldFolder = new File(Bukkit.getWorldContainer().getParent(), world.getName());
        File mapFolder = new File(REggWars.getInstance().getDataFolder(), "/data/worlds/" + world.getName());
        if (mapFolder.exists()) {
            FileUtils.delete(mapFolder);
        }
        FileUtils.copy(worldFolder, mapFolder);
    }

    @SneakyThrows
    public void copySlimeWorld(String name) {
        File worldFolder = new File("slime_worlds/", name + ".slime");
        File mapFolder = new File(REggWars.getInstance().getDataFolder(), "/data/slime/" + name + ".slime");
        if (mapFolder.exists()) {
            FileUtils.delete(mapFolder);
        }
        FileUtils.copy(worldFolder, mapFolder);

    }

    @SneakyThrows
    public void copySlimeWorldButDontDelete(String name, String newName) {
        File mapToCopy = new File(REggWars.getInstance().getDataFolder(), "/data/slime/" + name + ".slime");
        if (!mapToCopy.exists()) return;
        File newMap = new File(REggWars.getInstance().getDataFolder(), "/data/slime/" + newName + ".slime");
        FileUtils.copy(mapToCopy, newMap);
        File worldToCreate = new File("slime_worlds/", newName + ".slime");
        FileUtils.copy(mapToCopy, worldToCreate);
    }

    @SneakyThrows
    public void copyWorldButDontDelete(String name, String newName) {
        File mapToCopy = new File(REggWars.getInstance().getDataFolder(), "/data/worlds/" + name);
        if (!mapToCopy.exists()) return;
        File newMap = new File(REggWars.getInstance().getDataFolder(), "/data/worlds/" + newName);
        FileUtils.copy(mapToCopy, newMap);

        File worldToCreate = new File(Bukkit.getWorldContainer().getParentFile(), newName);
        FileUtils.copy(mapToCopy, worldToCreate);
        createNewWorld(newName);
    }

    public void removeSlimeWorld(World world) {
        if (world == null) return;
        Bukkit.unloadWorld(world, false);
        try {
            REggWars.getInstance().getLoader().deleteWorld(world.getName());
        } catch (UnknownWorldException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importWorldToSlime(String name) {
        File bukkitWorld = new File(REggWars.getInstance().getDataFolder() + "/data/worlds/", name);
        if (bukkitWorld == null) {
            System.out.println("invalid world ?");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
            try {
                REggWars.getInstance().getSlime()
                        .importWorld(bukkitWorld, name, REggWars.getInstance().getLoader());
            } catch (WorldAlreadyExistsException | InvalidWorldException |
                     WorldLoadedException | WorldTooBigException |
                     IOException e) {
                throw new RuntimeException(e);
            }
            copySlimeWorld(name);
        });
    }

    public void removeWorld(World world) {
        if (world == null) return;
        Bukkit.unloadWorld(world, false);
        File worldFolder = new File(Bukkit.getWorldContainer().getParent(), world.getName());
        if (worldFolder == null) return;
        FileUtils.delete(worldFolder);
    }


}
