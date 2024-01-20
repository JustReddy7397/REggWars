package ga.justreddy.wiki.reggwars.nms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.List;

/**
 * @author JustReddy
 */
public interface Nms {

    boolean isLegacyVersion();

    void sendJsonMessage(Player player, String json);

    void sendTitle(Player player, String title, String subtitle);

    void sendActionbar(Player player, String actionBar);

    void spawnParticle(Location location,
                       String type, int amount, float offsetX,
                       float offsetY, float offsetZ, float data);

    boolean isParticleCorrect();

    ChunkGenerator getGenerator();

    void setWorldRule(World world, String rule, boolean value);

    void removeEntityAI(Entity entity);

    void setGravity(Entity entity, boolean gravity);

    void setPlayerListName(Player player, Player otherPlayer, String name);

    void setTeamName(IGameTeam team);

    void setTeamName(IGamePlayer player);

    void setWaitingLobbyName(IGamePlayer player);

    void removeWaitingLobbyName(IGame game);

    void removeWaitingLobbyName(IGame game, IGamePlayer player);

    Block getRelative(Location location);

    void spawnVillager(Location location);

    default Hologram spawnHologram(
            String name,
            Location location,
            IGamePlayer player, List<String> lines) {
        Hologram hologram = DHAPI.createHologram(name, location, false);
        if (player != null) {
            hologram.setDefaultVisibleState(false);
            hologram.setShowPlayer(player.getPlayer());
        }
        DHAPI.setHologramLines(hologram, lines);
        return hologram;
    }

    void setBlastProofItems();

}
