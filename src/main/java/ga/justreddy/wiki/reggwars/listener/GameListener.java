package ga.justreddy.wiki.reggwars.listener;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author JustReddy
 */
public class GameListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDragonEggBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) return;
        if (gamePlayer.isDead()) return;
        IGame game = gamePlayer.getGame();
        if (game == null) return;
        IGameTeam team = gamePlayer.getTeam();
        if (team == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clickedBlock = event.getClickedBlock();
        if (event.getClickedBlock().getType() != XMaterial.DRAGON_EGG.parseMaterial()) return;
        event.setCancelled(true);
        Location location = clickedBlock.getLocation();
        Location ownEggLocation = team.getEggLocation();
        if (LocationUtils.equalsBlock(location, ownEggLocation) && !team.isEggGone()) {
            // TODO send message :)
            return;
        }

        IGameTeam teamEgg = game.getTeamByEggLocation(location);
        if (teamEgg == null) return;

        // TODO send message :)
        teamEgg.setEggGone(true);
        clickedBlock.setType(Material.AIR);

    }

}
