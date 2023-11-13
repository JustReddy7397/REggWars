package ga.justreddy.wiki.reggwars.api.model.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Location;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IHologram {

    IHologram spawn(IGamePlayer player);

    IHologram despawn(IGamePlayer player);

    IHologram withLine(String line);

    IHologram updateLine(IGamePlayer player, int id, String line);

    boolean isSpawned();

    Location getLocation();

    List<IHologramLine> getLines();

}
