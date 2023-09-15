package ga.justreddy.wiki.reggwars.listener;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
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
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block clickedBlock = event.getClickedBlock();
        if (event.getClickedBlock().getType() != XMaterial.DRAGON_EGG.parseMaterial()) return;
        event.setCancelled(true);
        Location location = clickedBlock.getLocation();
        Location ownEggLocation = team.getEggLocation();
        if (LocationUtils.equalsBlock(location, ownEggLocation) && !team.isEggGone()) {
            gamePlayer.sendLegacyMessage("&cYou can't break your own egg silly"); // TODO
            return;
        }

        IGameTeam teamEgg = game.getTeamByEggLocation(location);
        if (teamEgg == null) return;

        // TODO send message :)
        teamEgg.setEggGone(true);
        clickedBlock.setType(Material.AIR);
        game.sendLegacyMessage("haha " + teamEgg.getTeam().getDisplayName() + " their egg has been broken, what a nerd!");
    }

    @EventHandler(ignoreCancelled = true)
    public void onGeneratorInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        IGame game = gamePlayer.getGame();
        if (game == null) return;
        Location location = event.getClickedBlock().getLocation();
        IGenerator generator = game.getGeneratorByLocation(location);
        if (generator == null) return;
        if (player.isSneaking() && !generator.isMaxLevel()) {
            // TODO upgrade generator if able
            return;
        }
        // TODO open GUI
    }

}
