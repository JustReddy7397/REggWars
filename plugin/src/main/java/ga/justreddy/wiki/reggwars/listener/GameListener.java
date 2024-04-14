package ga.justreddy.wiki.reggwars.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.events.EggWarsEvent;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillEffect;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShop;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.security.interfaces.RSAKey;
import java.util.ArrayList;

/**
 * @author JustReddy
 */
public class GameListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDragonEggBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) return;
        IGame game = gamePlayer.getGame();
        if (game == null) return;
        if (gamePlayer.isDead() || gamePlayer.isFakeDead()) return;
        IGameTeam team = gamePlayer.getTeam();
        if (team == null) return;
        Block clickedBlock = event.getClickedBlock();
        if (event.getClickedBlock().getType() != XMaterial.DRAGON_EGG.parseMaterial()) return;
        event.setCancelled(true);
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Location location = clickedBlock.getLocation();
        Location ownEggLocation = team.getEggLocation();
        if (LocationUtils.equalsBlock(location, ownEggLocation) && !team.isEggGone()) {
            gamePlayer.sendMessage(Message.MESSAGES_GAME_CANT_BREAK_OWN_EGG); // TODO
            return;
        }

        IGameTeam teamEgg = game.getTeamByEggLocation(location);
        if (teamEgg == null) return;

        teamEgg.setEggGone(true);
        clickedBlock.setType(Material.AIR);
        KillMessage def = KillMessageManager.getManager().getById(0);
        def.sendEggBreakMessage(game, gamePlayer, teamEgg);
    }

    @EventHandler
    public void onShopInteractEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer == null) return;
        IGame game = gamePlayer.getGame();
        if (game == null) return;
        if (gamePlayer.isDead() || gamePlayer.isFakeDead()) return;
        IGameTeam team = gamePlayer.getTeam();
        if (team == null) return;
        if (event.getRightClicked() == null) return;
        if (event.getRightClicked().getType() != EntityType.VILLAGER) return;
        IShop shop = game.getShopByLocation(event.getRightClicked().getLocation());
        if (shop == null) return;
        event.setCancelled(true);
        Bukkit.getScheduler().runTask(REggWars.getInstance(), () -> shop.open(gamePlayer));
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
        if (game.getGeneratorByLocation(location.clone().subtract(0, 1, 0)) != null) {
            player.sendMessage(Message.MESSAGES_GAME_CANT_PLACE_BLOCKS_GENERATOR);
            event.setCancelled(true);
            return;
        }

        game.addBlock(location);

    }


    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(EntityExplodeEvent event) {
        World world = event.getLocation().getWorld();
        IGame game = GameManager.getManager().getGameByWorld(world);
        if (game == null) return;
        for (Block block : new ArrayList<>(event.blockList())) {
            if (game.isPlacedBlock(block.getLocation())) continue;
            event.blockList().remove(block);
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
            player.sendMessage(Message.MESSAGES_GAME_CANT_BREAK_BLOCKS);
            event.setCancelled(true);
            return;
        }

        game.removeBlock(event.getBlock().getLocation());

    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getEntity().getUniqueId());
        if (player == null) return;
        IGame game = player.getGame();
        if (game == null) return;
        if (!game.isGameState(GameState.PLAYING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {
            IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getEntity().getUniqueId());
            if (player == null) return;
            IGame game = player.getGame();
            if (game == null) return;
            IGamePlayer target = null;
            if (event.getDamager() instanceof Player) {
                target = PlayerManager.getManager().getGamePlayer(event.getDamager().getUniqueId());
            } else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (!(projectile.getShooter() instanceof Player)) return;
                target = PlayerManager.getManager().getGamePlayer(((Player) projectile.getShooter()).getUniqueId());

            }

            if (target == null) return;
            if (player.getGame() == null) return;
            if (target.getGame() == null) return;
            if (event.getCause() == null) return;
            if (target == player) return;
            if (player.getTeam() == target.getTeam()) {
                event.setCancelled(true);
                return;
            }

            if (game.hasRespawnProtection(target) && !game.hasRespawnProtection(player)) {
                game.removeRespawnProtection(target);
            }

            if (game.hasRespawnProtection(player)) {
                event.setCancelled(true);
                return;
            }

            player.getCombatLog().setTarget(target);

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDroppedExp(0);
        event.setDeathMessage(null);
        Player death = event.getEntity();
        if (death == null) return;
        IGamePlayer deathPlayer = PlayerManager.getManager().getGamePlayer(death.getUniqueId());
        if (deathPlayer == null) return;
        IGame game = deathPlayer.getGame();
        if (game == null) return;
        Player killer = death.getKiller();
        IGamePlayer killerPlayer = null;
        if (killer != null) {
            killerPlayer = PlayerManager.getManager().getGamePlayer(killer.getUniqueId());
        } else if (deathPlayer.getCombatLog().hasTarget() && deathPlayer.getCombatLog().isTimeCorrect()) {
            killerPlayer = deathPlayer.getCombatLog().getTarget();
        }

        EntityDamageEvent.DamageCause damageCause = death.getPlayer().getLastDamageCause().getCause();
        String path;
        switch (damageCause) {
            case VOID:
                path = "void";
                break;
            case FALL:
                path = "fall";
                if (deathPlayer.getPlayer().getLocation().getBlockY() <= game.getGameRegion().getLowY()) {
                    path = "void";
                }
                break;
            case FIRE_TICK:
                path = "burned";
                break;
            case ENTITY_ATTACK:
                path = "melee";
                break;
            case LAVA:
                path = "lava";
                break;
            case BLOCK_EXPLOSION:
                path = "explosion";
                break;
            case DROWNING:
                path = "drowning";
                break;
            case SUFFOCATION:
                path = "suffocation";
                break;
            case PROJECTILE:
                path = "projectile";
                if (deathPlayer.getPlayer().getLocation().getBlockY() <= game.getGameRegion().getLowY()) {
                    path = "void";
                }
                break;
            default:
                path = "unknown";
                break;
        }
        if (killerPlayer == null) {
            game.onGamePlayerDeath(null, deathPlayer, path,
                    deathPlayer.getTeam() != null && deathPlayer.getTeam().isEggGone()
            );
        } else {
            game.addKill(killerPlayer);
            killerPlayer.sendSound(
                    XSound.ENTITY_EXPERIENCE_ORB_PICKUP.name()
            );
            // TODO add stats
            game.onGamePlayerDeath(killerPlayer, deathPlayer, path, deathPlayer.getTeam().isEggGone());
            // TODO add kill effect
        }

    }

}
