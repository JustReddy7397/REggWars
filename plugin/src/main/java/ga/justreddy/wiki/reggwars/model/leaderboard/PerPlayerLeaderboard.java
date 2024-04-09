package ga.justreddy.wiki.reggwars.model.leaderboard;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
@Getter
public abstract class PerPlayerLeaderboard extends Leaderboard{

    protected final Map<GamePlayer, Hologram> leaderboards;

    public PerPlayerLeaderboard(String id, Location location, LeaderboardType type) {
        super(id, location, type);
        this.leaderboards = new HashMap<>();
    }

    public void remove(GamePlayer player) {
        if (!leaderboards.containsKey(player)) return;
        Hologram hologram = leaderboards.getOrDefault(player, null);
        if (hologram != null) {
            hologram.destroy();
        }
        leaderboards.remove(player);
    }

    public void set(GamePlayer player, List<String> originalLines) {
        /*if (leaderboards.containsKey(player)) return;
        Hologram hologram = new Hologram(originalLines, location);
        leaderboards.put(player, hologram);*/
    }

}
