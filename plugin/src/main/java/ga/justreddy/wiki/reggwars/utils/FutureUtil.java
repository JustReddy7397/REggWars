package ga.justreddy.wiki.reggwars.utils;

import ga.justreddy.wiki.reggwars.REggWars;
import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


/**
 * @author JustReddy
 */
public class FutureUtil {

    public static <T> CompletableFuture<T> future(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return supplier.call();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new CompletionException(e);
                    }
                }, r -> Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(REggWars.getInstance(), r)
        );
    }

    public static <T> CompletableFuture<T> futureAsync(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return supplier.call();
                    } catch (Exception e) {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        }
                        throw new CompletionException(e);
                    }
                }, r -> Bukkit.getServer().getScheduler().runTaskAsynchronously(REggWars.getInstance(), r)
        );
    }

    public static CompletableFuture<Void> future(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        }, r -> Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(REggWars.getInstance(), r));
    }

    public static CompletableFuture<Void> futureAsync(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        }, r -> Bukkit.getServer().getScheduler().runTaskAsynchronously(REggWars.getInstance(), r));
    }


    public static <T> CompletableFuture<T> delayedAsync(Callable<T> supplier, int ticks) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        }, r -> Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(REggWars.getInstance(), r, ticks));
    }

    public static CompletableFuture<Void> delayedAsync(Runnable runnable, int ticks) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw (RuntimeException) e;
            }
        }, r -> Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(REggWars.getInstance(), r, ticks));
    }

}
