package ga.justreddy.wiki.reggwars.api.model.game;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * @author JustReddy
 */
public abstract class ResetAdapter {

    private final Plugin owner;

    public ResetAdapter(Plugin owner) {
        this.owner = owner;
    }

    public Plugin getOwner() {
        return owner;
    }

    public abstract void onEnable(IGame game);

    public abstract void onRestart(IGame game);

    public abstract void onDisable(IGame game);

    public abstract boolean doesWorldExist(String world);

}
