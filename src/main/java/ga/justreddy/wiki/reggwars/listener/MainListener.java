package ga.justreddy.wiki.reggwars.listener;

import com.avaje.ebeaninternal.server.query.LimitOffsetList;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.manager.MapManager;
import ga.justreddy.wiki.reggwars.manager.MenuManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.gui.custom.Gui;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.packets.FakeTeamManager;
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
        PlayerManager.getManager().
                addGamePlayer(player.getUniqueId(), player.getName());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null) return;
        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(event.getWhoClicked().getUniqueId());
        // TODO check if player is building
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder == null) return;
        if (event.getCurrentItem() == null) return;
        if (holder instanceof Gui) {
            Gui gui = (Gui) holder;
            gui.inventoryClick(event);
        } else if (holder instanceof InventoryMenu) {
            InventoryMenu menu = (InventoryMenu) holder;
            menu.inventoryClick(event);
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
        FakeTeamManager.getPlayerTeams().remove(gamePlayer.getUniqueId());
        PlayerManager.getManager().removeGamePlayer(gamePlayer.getUniqueId());
    }

}
