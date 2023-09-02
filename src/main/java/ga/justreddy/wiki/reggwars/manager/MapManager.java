package ga.justreddy.wiki.reggwars.manager;

import com.sun.javafx.scene.control.skin.RadioButtonSkin;
import ga.justreddy.wiki.reggwars.REggWars;
import lombok.Getter;

import java.io.File;

/**
 * @author JustReddy
 */
@Getter
public class MapManager {

    private static MapManager manager;

    public static MapManager getManager() {
        return manager == null ? manager = new MapManager() : manager;
    }

    private final File flatWorldFolder;
    private final File slimeWorldFolder;

    public MapManager() {
        this.flatWorldFolder = new File(
                REggWars.getInstance().getDataFolder() + "/data/worlds"
        );
        this.slimeWorldFolder = new File(
                REggWars.getInstance().getDataFolder() + "/data/slime"
        );
        if (!flatWorldFolder.exists()) flatWorldFolder.mkdirs();
        if (!slimeWorldFolder.exists()) slimeWorldFolder.mkdirs();
    }



}
