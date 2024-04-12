package ga.justreddy.wiki.reggwars.api.model.leaderboard;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public abstract class PerPlayerLeaderboard extends Leaderboard {

    public PerPlayerLeaderboard(String id) {
        super(id);
    }

    public abstract void update(IGamePlayer player);

}
