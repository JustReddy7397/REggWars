package ga.justreddy.wiki.reggwars.model.leaderboard.impl;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.leaderboard.LeaderboardType;
import ga.justreddy.wiki.reggwars.model.leaderboard.PerPlayerLeaderboard;
import org.bukkit.Location;

import java.util.Map;

/**
 * @author JustReddy
 */
public class KillsLeaderBoard extends PerPlayerLeaderboard {


    public KillsLeaderBoard(String id, Location location, LeaderboardType type) {
        super(id, location, type);
    }

    @Override
    public void update() {
        for (Map.Entry<GamePlayer, Hologram> entry : leaderboards.entrySet()) {
            GamePlayer player = entry.getKey();
            if (entry.getKey().getPlayer() == null) {
                remove(player);
                continue;
            }
            Hologram hologram = entry.getValue();
            hologram.update(player.getPlayer());
        }
    }

    @Override
    public void destroy() {

    }

}
