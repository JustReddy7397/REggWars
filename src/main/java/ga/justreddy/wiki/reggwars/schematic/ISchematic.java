package ga.justreddy.wiki.reggwars.schematic;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;

/**
 * For cages possibly ?
 * @author JustReddy
 */
public interface ISchematic {

    String getPrefix();

    void save(File file, Location low, Location high);

    Object get(File file, World world);

    void paste(Object schematic, Location location);

}
