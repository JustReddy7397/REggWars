package ga.justreddy.wiki.reggwars.api.model.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IHologram {

    /**
     * Spawn the hologram for all players
     * @return the hologram that spawned
     */
    IHologram spawn();

    /**
     * Spawn the hologram for a world
     * @param world the world
     * @return the hologram that spawned
     */
    IHologram spawn(World world);

    /**
     * Spawn the hologram for a player
     * @param player the player
     * @return the hologram that spawned
     */
    IHologram spawn(IGamePlayer player);

    /**
     * Despawn the hologram for all players1
     * @return the hologram that despawned
     */
    IHologram despawn();

    /**
     * Despawn the hologram for a world
     * @param world the world
     * @return the hologram that despawned
     */
    IHologram despawn(World world);

    /**
     * Despawn the hologram for a player
     * @param player the player
     * @return the hologram that despawned
     */
    IHologram despawn(IGamePlayer player);

    /**
     * Add a line to the hologram
     * @param line the line
     * @return the hologram with the line
     */
    IHologram withLine(String line);

    /**
     * Add a line to the hologram
     * @param id the id of the line
     * @param line the line
     * @return the hologram with the line
     */
    IHologram updateLine(int id, String line);

    /**
     * Update a line in the hologram
     * @param world the world
     * @param id the id of the line
     * @param line the new line
     * @return the hologram with the updated line
     */
    IHologram updateLine(World world, int id, String line);

    /**
     * Update a line in the hologram
     * @param player the player
     * @param id the id of the line
     * @param line the new line
     * @return the hologram with the updated line
     */
    IHologram updateLine(IGamePlayer player, int id, String line);

    /**
     * Check if the hologram is spawned
     * @return true if spawned else false
     */
    boolean isSpawned();

    /**
     * Get the location of the hologram
     * @return the location
     */
    Location getLocation();

    /**
     * Get the lines of the hologram
     * @return the lines
     */
    List<IHologramLine> getLines();

}
