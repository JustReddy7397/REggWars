package ga.justreddy.wiki.reggwars.model.leaderboard;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.PerPlayerLeaderboard;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import org.bukkit.Location;

import java.util.Map;

/**
 * @author JustReddy
 */
public class KillsLeaderBoard extends PerPlayerLeaderboard {

    public KillsLeaderBoard(String id, Location location) {
        super(id, location);
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update(IGamePlayer player) {

    }


    /*public KillsLeaderBoard(String id, Location location, LeaderboardType type) {
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

    }*/

}
