package ga.justreddy.wiki.reggwars.api.model.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Location;

/**
 * @author JustReddy
 */
public interface IHologramLine {

    void spawn(IGamePlayer player);

    void despawn(IGamePlayer player);

    void setLocation(Location location);

    void setLine(String line);

    Location getLocation();

    IArmorStand getStand();

    String getLine();

    IHologram getHologram();

}
