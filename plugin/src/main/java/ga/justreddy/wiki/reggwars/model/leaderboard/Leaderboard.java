package ga.justreddy.wiki.reggwars.model.leaderboard;

import lombok.Getter;
import org.bukkit.Location;

/**
 * @author JustReddy
 */
@Getter
public abstract class Leaderboard {

    protected String id;
    protected Location location;
    protected LeaderboardType type;

    public Leaderboard(String id, Location location, LeaderboardType type) {
        this.id = id;
        this.location = location;
        this.type = type;
    }

    public abstract void update();

    public abstract void destroy();

}
