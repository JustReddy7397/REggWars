package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
