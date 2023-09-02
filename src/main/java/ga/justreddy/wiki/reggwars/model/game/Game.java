package ga.justreddy.wiki.reggwars.model.game;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author JustReddy
 */
public class Game implements IGame {

    private final String name;
    private final FileConfiguration config;
    private World world;

    public Game(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init(World world) {
        this.world = world;
    }
}
