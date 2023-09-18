package ga.justreddy.wiki.reggwars.listener;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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
        Block clickedBlock = event.getClickedBlock();
        if (event.getClickedBlock().getType() != XMaterial.DRAGON_EGG.parseMaterial()) return;
        event.setCancelled(true);
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Location location = clickedBlock.getLocation();
        Location ownEggLocation = team.getEggLocation();
        if (LocationUtils.equalsBlock(location, ownEggLocation) && !team.isEggGone()) {
            gamePlayer.sendLegacyMessage("&cYou can't break your own egg silly"); // TODO
            return;
        }

        IGameTeam teamEgg = game.getTeamByEggLocation(location);
        if (teamEgg == null) return;

        teamEgg.setEggGone(true);
        clickedBlock.setType(Material.AIR);
        KillMessage def = KillMessageManager.getManager().getById(0);
        // TODO
        def.sendEggBreakMessage(game, gamePlayer, teamEgg);
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        if (player.getGame() == null) return;
        IGame game = player.getGame();
        if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
            event.setCancelled(true);
            return;
        }

        Location location = event.getBlock().getLocation();
        if (game.getGeneratorByLocation(location.clone().subtract(0, 1,0)) != null) {
            player.sendLegacyMessage("&cYou can't place blocks on a generator"); // TODO
            return;
        }

        game.addBlock(location);

    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            World w = block.getWorld();
            IGame game = GameManager.getManager().getGameByWorld(w);
            if (game == null) continue;
            if (game.getGeneratorByLocation(block.getLocation()) != null) {

            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        if (player.getGame() == null) return;
        IGame game = player.getGame();
        if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING)) {
            event.setCancelled(true);
            return;
        }

        if (!game.isPlacedBlock(event.getBlock().getLocation())) {
            player.sendLegacyMessage("&cYou can only place blocks placed by players!"); // TODO
            event.setCancelled(true);
            return;
        }

        game.removeBlock(event.getBlock().getLocation());


    }

}
