package ga.justreddy.wiki.reggwars.api.model.game;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * @author JustReddy
 */
public interface IGameSign {

    String getId();

    Location getLocation();

    IGame getGame();

    void update();

    Block getRelative();

}
