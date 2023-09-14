package ga.justreddy.wiki.reggwars.model.gui.editable;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
public abstract class InventoryMenu implements InventoryHolder {

    protected Inventory inventory;

    public abstract String name();

    public abstract String identifier();

    public abstract int rows();

    public abstract int refreshRate();

    public abstract boolean isRefreshEnabled();

    public abstract String placeholders(String text, IGamePlayer gamePlayer);

    public abstract void setMenuItems(IGamePlayer gamePlayer);

    public abstract void inventoryClick(InventoryClickEvent e);

    public void onClose() {
        if (task != null) task.cancel();
    }

    protected BukkitTask task;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open(IGamePlayer gamePlayer) {
        inventory = Bukkit.createInventory(this, 9 * rows(), ChatUtil.format(name()));
        gamePlayer.getPlayer().openInventory(inventory);
        if (!Bukkit.isPrimaryThread()) {
            setMenuItems(gamePlayer);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
                setMenuItems(gamePlayer);
            });
        }
        if (isRefreshEnabled()) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(REggWars.getInstance(), () -> {
                refresh(gamePlayer);
            }, 20, refreshRate() * 20L);
        }
    }

    public void refresh(IGamePlayer player) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null || itemStack.getType() == Material.AIR || !itemStack.hasItemMeta()) continue;
            ItemStack clonedItem = itemStack.clone();
            ItemBuilder newItem = new ItemBuilder(clonedItem.clone());
            if (clonedItem.getItemMeta().hasDisplayName()) newItem.withName(
                    placeholders(clonedItem.getItemMeta().getDisplayName(), player));
            if (clonedItem.getItemMeta().hasLore()){
                List<String> lore = clonedItem.getItemMeta().getLore();
                List<String> newLore = new ArrayList<>();
                lore.forEach(l -> {
                    newLore.add(placeholders(l, player));
                });
                newItem.withLore(
                        newLore
                );
            }
            if (clonedItem.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
                SkullMeta meta = (SkullMeta) clonedItem.getItemMeta();
                if (!newItem.isHasTexture() && newItem.isHasUsername()) newItem.setSkullOwner(player.getName());
                if (!newItem.isHasUsername() && newItem.isHasTexture()) newItem.withBase64(newItem.getTexture());
            }
            inventory.setItem(i, newItem.build());

        }
    }


}
