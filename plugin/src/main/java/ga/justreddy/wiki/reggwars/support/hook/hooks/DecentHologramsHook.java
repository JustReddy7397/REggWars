package ga.justreddy.wiki.reggwars.support.hook.hooks;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.support.hook.Hook;
import org.bukkit.Bukkit;

/**
 * @author JustReddy
 */
public class DecentHologramsHook implements Hook {

    @Override
    public String getHookId() {
        return "DecentHolograms";
    }

    @Override
    public boolean canHook() {
        return Bukkit.getPluginManager().getPlugin("DecentHolograms") != null;
    }

    @Override
    public void hook(REggWars plugin) {
        // Empty
    }

    @Override
    public void disable(REggWars plugin) {
        // Empty
    }
}
