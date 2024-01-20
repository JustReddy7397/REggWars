package ga.justreddy.wiki.reggwars.listener;

import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.manager.MenuManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopGui;
import ga.justreddy.wiki.reggwars.model.gui.custom.Gui;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.packets.FakeTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author JustReddy
 */
public class MainListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IGamePlayer gamePlayer = PlayerManager.getManager().
                addGamePlayer(player.getUniqueId(), player.getName());
        event.setJoinMessage(null);
        if (Core.MODE != ServerMode.BUNGEE) {
            if (REggWars.getInstance().getSpawnLocation() != null) {
                player.teleport(REggWars.getInstance().getSpawnLocation());
            }
            // TODO more stuff :)
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null) return;
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(event.getWhoClicked().getUniqueId());
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder == null) return;
        if (event.getCurrentItem() == null) return;
        if (holder instanceof Gui) {
            Gui gui = (Gui) holder;
            event.setCancelled(true);
            gui.inventoryClick(event);
        } else if (holder instanceof InventoryMenu) {
            InventoryMenu menu = (InventoryMenu) holder;
            event.setCancelled(true);
            menu.onClick(gamePlayer, event);
        } else if (holder instanceof ShopGui) {
            event.setCancelled(true);
            ShopGui shopGui = (ShopGui) holder;
            shopGui.onClick(gamePlayer, event.getRawSlot(), event.isShiftClick());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() == null) return;
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        InventoryMenu menu = MenuManager.getManager().getOpenMenus().getOrDefault(gamePlayer, null);
        if (menu != null) {
            menu.onClose();
            MenuManager.getManager().getOpenMenus().remove(gamePlayer);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(event.getPlayer().getUniqueId());
        InventoryMenu menu = MenuManager.getManager().getOpenMenus().getOrDefault(gamePlayer, null);
        if (menu != null) {
            menu.onClose();
            MenuManager.getManager().getOpenMenus().remove(gamePlayer);
        }
        if (gamePlayer.getGame() != null) {
            IGame game = gamePlayer.getGame();
            game.onGamePlayerQuit(gamePlayer, true, false); // TODO
        }
        Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
            /*REggWars.getInstance().getStorage().savePlayer(gamePlayer);*/
            FakeTeamManager.getPlayerTeams().remove(gamePlayer.getUniqueId());
            PlayerManager.getManager().removeGamePlayer(gamePlayer.getUniqueId());
        });
    }

}
