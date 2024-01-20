package ga.justreddy.wiki.reggwars.model.gui.custom;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public abstract class Gui implements InventoryHolder {

    protected Inventory inventory;

    protected int page = 0;

    protected int index = 0;

    protected int maxItemsPerPage = 0;

    protected ItemStack FILLER_GLASS = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

    public abstract String name();

    public abstract int size();

    public void open(IGamePlayer player) {
        inventory = Bukkit.createInventory(this, size(), ChatColor.translateAlternateColorCodes('&', name()));
        player.getPlayer().openInventory(inventory);
        if (!Bukkit.isPrimaryThread()) {
            setMenuItems(player);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
                setMenuItems(player);
            });
        }

    }

    public abstract void inventoryClick(InventoryClickEvent event);

    public abstract void setMenuItems(IGamePlayer player);


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected void addMenuBorder(Map<?,?> map) {
        ItemStack left;
        ItemMeta meta;
        if(page == 0){
            left = XMaterial.GRAY_DYE.parseItem();
            meta = left.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "You're on the first page");
        }else{
            left = XMaterial.LIME_DYE.parseItem();
            meta = left.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Left");
        }

        left.setItemMeta(meta);

        ItemStack right;
        ItemMeta rightMeta;
        if (!((index + 1) >= map.size())) {
            right = XMaterial.LIME_DYE.parseItem();
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.GREEN + "Right");
        } else {
            right = XMaterial.GRAY_DYE.parseItem();
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.RED + "You're on the last page");
        }

        right.setItemMeta(rightMeta);

        switch (size()) {
            case 18:
                setItem(0, FILLER_GLASS);
                setItem(8, FILLER_GLASS);
                maxItemsPerPage = 7;
                setItem(12, left);
                setItem(14, right);
                setItem(13, FILLER_GLASS);
                for (int i = 9; i < 18; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 27:
                // Seven because the row above the middle one will get glassed too!
                maxItemsPerPage = 7;
                setItem(17, FILLER_GLASS);
                setItem(9, FILLER_GLASS);
                setItem(21, left);
                setItem(22, FILLER_GLASS);
                setItem(23, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 18; i < 27; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 36:
                maxItemsPerPage = 14;
                setItem(9, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(17, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(30, left);
                setItem(31, FILLER_GLASS);
                setItem(32, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 27; i < 35; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 45:
                maxItemsPerPage = 21;
                setItem(9, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(27, FILLER_GLASS);
                setItem(17, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(35, FILLER_GLASS);
                setItem(39, left);
                setItem(40, FILLER_GLASS);
                setItem(41, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 36; i < 45; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 54:
                maxItemsPerPage = 28;
                setItem(17, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(27, FILLER_GLASS);
                setItem(35, FILLER_GLASS);
                setItem(36, FILLER_GLASS);
                setItem(48, left);
                setItem(49, FILLER_GLASS);
                setItem(50, right);
                for (int i = 0; i < 10; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 44; i < 54; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
        }
    }

    protected void addMenuBorder(List<?> list) {
        ItemStack left;
        ItemMeta meta;
        if(page == 0){
            left = XMaterial.GRAY_DYE.parseItem();
            meta = left.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "You're on the first page");
        }else{
            left = XMaterial.LIME_DYE.parseItem();
            meta = left.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Left");
        }

        left.setItemMeta(meta);

        ItemStack right;
        ItemMeta rightMeta;
        if (((index + 1) >= list.size())) {
            right = XMaterial.LIME_DYE.parseItem();
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.GREEN + "Right");

        } else {
            right = XMaterial.GRAY_DYE.parseItem();
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.RED + "You're on the last page");
        }

        right.setItemMeta(rightMeta);

        switch (size()) {
            case 18:
                setItem(0, FILLER_GLASS);
                setItem(8, FILLER_GLASS);
                maxItemsPerPage = 7;
                setItem(12, left);
                setItem(14, right);
                setItem(13, FILLER_GLASS);
                for (int i = 9; i < 18; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 27:
                // Seven because the row above the middle one will get glassed too!
                maxItemsPerPage = 7;
                setItem(17, FILLER_GLASS);
                setItem(9, FILLER_GLASS);
                setItem(21, left);
                setItem(22, FILLER_GLASS);
                setItem(23, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 18; i < 27; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 36:
                maxItemsPerPage = 14;
                setItem(9, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(17, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(30, left);
                setItem(31, FILLER_GLASS);
                setItem(32, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 27; i < 35; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 45:
                maxItemsPerPage = 21;
                setItem(9, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(27, FILLER_GLASS);
                setItem(17, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(35, FILLER_GLASS);
                setItem(39, left);
                setItem(40, FILLER_GLASS);
                setItem(41, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 36; i < 45; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 54:
                maxItemsPerPage = 28;
                setItem(17, FILLER_GLASS);
                setItem(18, FILLER_GLASS);
                setItem(26, FILLER_GLASS);
                setItem(27, FILLER_GLASS);
                setItem(35, FILLER_GLASS);
                setItem(36, FILLER_GLASS);
                setItem(48, left);
                setItem(49, FILLER_GLASS);
                setItem(50, right);
                for (int i = 0; i < 10; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 44; i < 54; i++) {
                    if (inventory.getItem(i) == null) {
                        setItem(i, FILLER_GLASS);
                    }
                }
        }
    }

    protected void removeMenuBorder() {
        for (int i = 0; i < size(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            if (item.getType() != FILLER_GLASS.getType()) continue;
            inventory.remove(item);
        }
    }

    public void setItem(int index, ItemStack itemStack) {
        inventory.setItem(index, itemStack);
    }

    public void setItem(int index, ItemBuilder itemBuilder) {
        inventory.setItem(index, itemBuilder.build());
    }

    public void addItem(ItemStack... itemStack) {
        inventory.addItem(itemStack);
    }

    public void addItem(ItemBuilder... itemBuilders) {
        for (ItemBuilder builder : itemBuilders) {
            addItem(builder.build());
        }
    }


}
