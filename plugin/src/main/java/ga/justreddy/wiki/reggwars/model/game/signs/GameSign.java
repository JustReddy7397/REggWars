package ga.justreddy.wiki.reggwars.model.game.signs;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * @author JustReddy
 */
public class GameSign implements IGameSign {

    private final String id;
    private final Location location;
    private final IGame game;

    public GameSign(String id, Location location, IGame game) {
        this.id = id;
        this.location = location;
        this.game = game;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public IGame getGame() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public Block getRelative() {
        return REggWars.getInstance().getNms().getRelative(location);
    }
}
