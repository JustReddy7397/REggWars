package ga.justreddy.wiki.reggwars.model.game.map;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.manager.MapManager;
import ga.justreddy.wiki.reggwars.manager.WorldManager;
import ga.justreddy.wiki.reggwars.socket.SocketClient;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class FlatAdapter extends ResetAdapter {

    private final IMessenger<?> client
            = REggWars.getInstance().getMessenger();


    private final boolean debug = REggWars
            .getInstance()
            .getSettingsConfig()
            .getConfig()
            .getBoolean("debug");


    public FlatAdapter() {
        super(REggWars.getInstance());
    }

    @Override
    public void onEnable(IGame game) {
        if (debug) {
            ((REggWars) getOwner()).log(Level.INFO, "Loading world for game " + game.getName());
        }
        File originalWorldFolder = new File(
                Bukkit.getWorldContainer(), game.getName()
        );
        File newWorldFolder = new File(
                MapManager.getManager().getFlatWorldFolder().getAbsolutePath()
                        + "/" + game.getName()
        );

        try {
            if (debug) {
                ((REggWars) getOwner()).log(Level.INFO, "Copying world for game " + game.getName());
            }
            FileUtils.copy(newWorldFolder, originalWorldFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String name = game.getName();
        World world = Bukkit.getWorld(name);
        if (world != null) {
            if (debug) {
                ((REggWars) getOwner()).log(Level.INFO, "Initializing game " + game.getName());
            }
            game.init(world);
            return;
        }

        if (debug) {
            ((REggWars) getOwner()).log(Level.INFO, "Creating world for game " + game.getName());
        }
        world = WorldManager.getManager().createNewWorld(game.getName());

        if (world != null) {
            if (debug) {
                ((REggWars) getOwner()).log(Level.INFO, "Initializing game " + game.getName());
            }
            game.init(world);
        }

    }

    @Override
    public void onRestart(IGame game) {
        if (debug) {
            ((REggWars) getOwner()).log(Level.INFO, "Restarting game " + game.getName());
        }
        onDisable(game);
        Bukkit.getScheduler().runTaskLater(
                REggWars.getInstance(),
                () -> onEnable(game),
                20L * 5);
    }

    @Override
    public void onDisable(IGame game) {
        if (debug) {
            ((REggWars) getOwner()).log(Level.INFO, "Unloading world for game " + game.getName());
        }
        World world = Bukkit.getWorld(game.getName());
        if (world == null && debug) {
            ((REggWars) getOwner()).log(Level.INFO, "World for game " + game.getName() + " is null");
            return;
        }
        Bukkit.unloadWorld(world, false);
        File originalWorldFolder = new File(
                Bukkit.getWorldContainer(), game.getName()
        );
        if (debug) {
            ((REggWars) getOwner()).log(Level.INFO, "Deleting world for game " + game.getName());
        }
        FileUtils.delete(originalWorldFolder);
    }

    @Override
    public boolean doesWorldExist(String world) {
        return Bukkit.getWorld(world) != null;
    }

}
