package ga.justreddy.wiki.reggwars.model.game.map;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.manager.MapManager;
import ga.justreddy.wiki.reggwars.manager.WorldManager;
import ga.justreddy.wiki.reggwars.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * @author JustReddy
 */
public class FlatAdapter extends ResetAdapter {

    public FlatAdapter() {
        super(REggWars.getInstance());
    }

    @Override
    public void onEnable(IGame game) {
        File originalWorldFolder = new File(
                Bukkit.getWorldContainer(), game.getName()
        );
        File newWorldFolder = new File(
                MapManager.getManager().getFlatWorldFolder().getAbsolutePath()
                        + "/" + game.getName()
        );

        try {
            FileUtils.copy(newWorldFolder, originalWorldFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String name = game.getName();
        World world = Bukkit.getWorld(name);
        if (world != null) {
            game.init(world);
            return;
        }

        world = WorldManager.getManager().createNewWorld(game.getName());

        if (world != null) game.init(world);

    }

    @Override
    public void onRestart(IGame game) {
        onDisable(game);
        onEnable(game);
    }

    @Override
    public void onDisable(IGame game) {
        Bukkit.unloadWorld(game.getName(), false);
        File originalWorldFolder = new File(
                Bukkit.getWorldContainer(), game.getName()
        );
        FileUtils.delete(originalWorldFolder);
    }

    @Override
    public boolean doesWorldExist(String world) {
        return Bukkit.getWorld(world) != null;
    }

}
