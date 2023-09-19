package ga.justreddy.wiki.reggwars.api.model.game;


import org.bukkit.Location;

/**
 * @author JustReddy
 */
public interface Region {

    boolean isInRegion(Location location);

    boolean isProtected();

    double getLowY();

}
