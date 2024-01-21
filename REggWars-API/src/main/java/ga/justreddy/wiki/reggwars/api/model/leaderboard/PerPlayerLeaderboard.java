package ga.justreddy.wiki.reggwars.api.model.leaderboard;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Location;

/**
 * @author JustReddy
 */
public abstract class PerPlayerLeaderboard extends Leaderboard {

    protected IGamePlayer player;

    public PerPlayerLeaderboard(String id, Location location, IGamePlayer player) {
        super(id, location);
        this.player = player;
    }

    public IGamePlayer getPlayer() {
        return player;
    }
}
