package ga.justreddy.wiki.reggwars.model.game.timer;

import ga.justreddy.wiki.reggwars.REggWars;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

/**
 * @author JustReddy
 */

@RequiredArgsConstructor
@Getter
public abstract class Timer implements Runnable {

    private final int seconds;
    protected int ticksExceed = 0;
    private int task;
    private final REggWars plugin;
    private boolean started;


    /**
     * Proper method to use to start the timer is start()
     */

    @Override
    public void run() {
        if (ticksExceed == 0) {
            onEnd();
            stop();
            return;
        }
        --ticksExceed;
        onTick();
    }

    public void start() {
        ticksExceed = seconds;
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
        started = true;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(task);
        started = false;
    }

    protected abstract void onTick();

    protected abstract void onEnd();


}
