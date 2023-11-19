package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.model.hologram.Hologram;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class HologramManager {

    private static HologramManager manager;

    public static HologramManager getManager() {
        return manager == null ? manager = new HologramManager() : manager;
    }

    private final Map<World, List<Hologram>> worldHolograms;

    private final List<Hologram> holograms;


    public HologramManager() {
        this.worldHolograms = new HashMap<>();
        this.holograms = new ArrayList<>();
    }

}
