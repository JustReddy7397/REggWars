package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class ShopGui implements IShopGui, InventoryHolder {

    private final FileConfiguration config;
    private final Map<Integer, IShopItem> items;
    private final Inventory inventory;

    public ShopGui(FileConfiguration config) {
        this.config = config;
        this.items = new HashMap<>();
        this.inventory = Bukkit.createInventory(null, config.getInt("rows") * 9, ChatUtil.format(config.getString("title")));
        loadItems();
    }

    @Override
    public List<IShopItem> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void onClick(IGamePlayer player, int slot) {
        IShopItem item = items.get(slot);
        if (item == null) return;
        item.give(player);
    }

    @Override
    public void open(IGamePlayer player) {
        final Player p = player.getPlayer();
        if (p == null) return;
        p.openInventory(inventory);
    }

    private void loadItems() {
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            IShopItem item = new ShopItem(config.getConfigurationSection("items." + key));
            items.put(item.getSlot(), item);
            inventory.setItem(item.getSlot(), item.getItem());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
