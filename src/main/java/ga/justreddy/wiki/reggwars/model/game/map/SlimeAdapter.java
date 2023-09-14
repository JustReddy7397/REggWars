package ga.justreddy.wiki.reggwars.model.game.map;

import com.google.gson.annotations.SerializedName;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.manager.MapManager;
import ga.justreddy.wiki.reggwars.utils.FileUtils;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SlimeAdapter extends ResetAdapter {

    private final SlimePlugin slime;
    private final SlimeLoader loader;

    public SlimeAdapter() {
        super(REggWars.getInstance());
        this.slime = REggWars.getInstance().getSlime();
        this.loader = REggWars.getInstance().getLoader();
    }

    @Override
    public void onEnable(IGame game) {
        Bukkit.getScheduler().runTask(getOwner(), () -> {

            File originalWorldFolder = new File(
                    "slime_worlds/" + game.getName() + ".slime"
            );
            File newWorldFolder = new File(
                    MapManager.getManager().getSlimeWorldFolder().getAbsolutePath()
                    + "/" + game.getName() + ".slime"
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

            Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
                try {
                    SlimeWorld slimeWorld = slime
                            .loadWorld(loader, name, true, getPropertyMap());


                    Bukkit.getScheduler().runTask(getOwner(), () -> {
                        slime.generateWorld(slimeWorld);
                        World generatedWorld = Bukkit.getWorld(name);
                        // TODO set rules
                        if (generatedWorld == null) {
                            onDisable(game);
                            return;
                        }

                        Bukkit.getScheduler().runTask(getOwner(), () -> {
                            game.init(generatedWorld);
                        });
                    });

                }catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                        WorldInUseException ex) {
                    onDisable(game);
                    ex.fillInStackTrace();
                }
            });

        });
    }

    @Override
    public void onRestart(IGame game) {
        Bukkit.getScheduler().runTask(getOwner(), () -> {
            Bukkit.unloadWorld(game.getName(), false);
            Bukkit.getScheduler().runTaskAsynchronously(getOwner(), () -> {
                File originalWorldFolder = new File(
                        "slime_worlds/" + game.getName() + ".slime"
                );
                FileUtils.delete(originalWorldFolder);
                try {
                    if (loader.worldExists(game.getName())) {
                        loader.deleteWorld(game.getName());
                    }
                }catch (UnknownWorldException | IOException e) {
                    Bukkit.getLogger().log(Level.WARNING, "Uh oh... " + game.getName());
                    e.fillInStackTrace();
                }
                onEnable(game);
            });
        });


    }

    @Override
    public void onDisable(IGame game) {
        Bukkit.getScheduler().runTask(getOwner(), () -> {
            Bukkit.unloadWorld(game.getName(), false);
            Bukkit.getScheduler().runTaskAsynchronously(getOwner(), () -> {
                File originalWorldFolder = new File(
                        "slime_worlds/" + game.getName() + ".slime"
                );
                FileUtils.delete(originalWorldFolder);
                try {
                    if (loader.worldExists(game.getName())) {
                        loader.deleteWorld(game.getName());
                    }
                }catch (UnknownWorldException | IOException e) {
                    Bukkit.getLogger().log(Level.WARNING, "Uh oh... " + game.getName());
                    e.fillInStackTrace();
                }
            });
        });
    }

    @SneakyThrows
    @Override
    public boolean doesWorldExist(String world) {
        return loader.worldExists(world);
    }

    private SlimePropertyMap getPropertyMap() {
        SlimePropertyMap map = new SlimePropertyMap();
        map.setInt(SlimeProperties.SPAWN_X, 0);
        map.setInt(SlimeProperties.SPAWN_Y, 30);
        map.setInt(SlimeProperties.SPAWN_Z, 0);
        map.setBoolean(SlimeProperties.PVP, true);
        map.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        map.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        map.setString(SlimeProperties.DIFFICULTY, "easy");
        return map;
    }
}
