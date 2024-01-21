package ga.justreddy.wiki.reggwars.api.model.leaderboard;

import org.bukkit.Location;

/**
 * @author JustReddy
 */
public abstract class Leaderboard {

    protected String id;
    protected Location location;

    public Leaderboard(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    public abstract void update();

    public abstract void destroy();

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }
}
