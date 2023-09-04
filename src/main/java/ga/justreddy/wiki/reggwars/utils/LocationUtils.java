package ga.justreddy.wiki.reggwars.utils;


import org.bukkit.Location;

/**
 * @author JustReddy
 */
public class LocationUtils {

    public static boolean equals(Location loc1, Location loc2) {
        return loc1.getX() == loc2.getX()
                && loc1.getY() == loc2.getY()
                && loc1.getZ() == loc2.getZ();
    }

    public static boolean equalsBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }


}
