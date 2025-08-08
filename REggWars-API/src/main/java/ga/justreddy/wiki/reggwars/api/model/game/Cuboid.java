package ga.justreddy.wiki.reggwars.api.model.game;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author JustReddy
 */

public class Cuboid implements Region {

    private final boolean protect;
    private final World world;
    private final int x1;
    private final int y1;
    private final int z1;
    private final int x2;
    private final int y2;
    private final int z2;
    private final Location lowPoints;
    private boolean cleared;

    public Cuboid(Location highPoints, Location lowPoints, boolean protect) {
        this.world = highPoints.getWorld();
        this.lowPoints = lowPoints;
        this.x1 = Math.min(highPoints.getBlockX(), lowPoints.getBlockX());
        this.y1 = Math.min(highPoints.getBlockY(), lowPoints.getBlockY());
        this.z1 = Math.min(highPoints.getBlockZ(), lowPoints.getBlockZ());
        this.x2 = Math.max(highPoints.getBlockX(), lowPoints.getBlockX());
        this.y2 = Math.max(highPoints.getBlockY(), lowPoints.getBlockY());
        this.z2 = Math.max(highPoints.getBlockZ(), lowPoints.getBlockZ());
        Location highPoints1 = new Location(this.world, this.x2, this.y2, this.z2);
        Location lowPoints1 = new Location(this.world, this.x1, this.y1, this.z1);
        this.protect = protect;
        this.cleared = false;
    }


    // TODO OVERHAUL TO USE WITH WORLDEDIT
    public void clear() {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                for (int z = z1; z < z2; z++) {
                    Block block = this.world.getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        cleared = true;
    }

    @Override
    public boolean isInRegion(Location location) {
        return location.getWorld() == world
                && location.getBlockX() >= x1
                && location.getBlockX() <= x2
                && location.getBlockY() <= y2
                && location.getBlockZ() >= z1
                && location.getBlockZ() <= z2;
    }

    @Override
    public boolean isProtected() {
        if (cleared) return false;
        return protect;
    }

    @Override
    public double getLowY() {
        return lowPoints.getY();
    }
}
