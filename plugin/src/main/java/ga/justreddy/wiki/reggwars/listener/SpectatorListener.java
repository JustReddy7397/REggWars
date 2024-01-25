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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;

/**
 * @author JustReddy
 */
public class SpectatorListener implements Listener {

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
        if (gamePlayer.isDead() || gamePlayer.isFakeDead()) {
            event.setCancelled(true);
            return;
        }
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

        if (player.isDead() || player.isFakeDead()) {
            event.setCancelled(true);
            return;
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

        if (player.isDead() || player.isFakeDead()) {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getEntity().getUniqueId());
        if (player == null) return;
        IGame game = player.getGame();
        if (game == null) return;
        if (!player.isDead() || !player.isFakeDead()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItemEvent(PlayerPickupItemEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        if (player == null) return;
        IGame game = player.getGame();
        if (game == null) return;
        if (!player.isDead() || !player.isFakeDead()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDropEvent(PlayerDropItemEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        if (player == null) return;
        IGame game = player.getGame();
        if (game == null) return;
        if (!player.isDead() || !player.isFakeDead()) return;
        event.setCancelled(true);
    }

}
