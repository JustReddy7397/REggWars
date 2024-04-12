package ga.justreddy.wiki.reggwars.api.model.leaderboard;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public abstract class Leaderboard {

    protected String id;
    protected Location location;

    public Leaderboard(String id) {
        this.id = id;
    }

    public abstract void update();

    public abstract void destroy();

    public String getId() {
        return id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
