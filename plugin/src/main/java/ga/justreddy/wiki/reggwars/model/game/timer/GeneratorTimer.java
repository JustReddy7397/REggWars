package ga.justreddy.wiki.reggwars.model.game.timer;

import ga.justreddy.wiki.reggwars.REggWars;
import org.bukkit.Bukkit;

/**
 * @author JustReddy
 */
public class GeneratorTimer extends Timer{

    public GeneratorTimer(int seconds, REggWars plugin) {
        super(seconds, plugin);
    }

    @Override
    public void start() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, 20, 1);
    }

    @Override
    protected void onTick() {

    }

    @Override
    protected void onEnd() {

    }
}
