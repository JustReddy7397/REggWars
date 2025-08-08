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
    protected int task;
    private final REggWars plugin;
    protected boolean started;

    /**
     * The Proper method to use to start the timer is start()
     */

    @Override
    public void run() {
        if (!isStarted()) {
            throw new IllegalStateException("Timer is not started. Use start() method to start the timer.");
        }
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
