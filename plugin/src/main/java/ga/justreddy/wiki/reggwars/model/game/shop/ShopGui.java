package ga.justreddy.wiki.reggwars.model.game.shop;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
    private final Map<Integer, IShopItem> quickBuy;
    private final Inventory inventory;

    public ShopGui(FileConfiguration config) {
        this.config = config;
        this.items = new HashMap<>();
        this.quickBuy = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, config.getInt("rows") * 9, ChatUtil.format(config.getString("title")));
        loadItems();
    }

    @Override
    public List<IShopItem> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void onClick(IGamePlayer player, int slot, boolean shift) {
        IShopItem item = items.get(slot);
        if (item == null) {
            IShopItem quick = quickBuy.get(slot);
            if (quick == null) return;
            quick.give(this, player, shift);
            return;
        }
        item.give(this, player, shift);
    }

    @Override
    public void open(IGamePlayer player) {
        final Player p = player.getPlayer();
        if (p == null) return;
        p.openInventory(inventory);
        if (config.isSet("quick-buy-slots")) {
            Map<Integer, IShopItem> quickBuy = new HashMap<>();
            ConfigurationSection section = REggWars.getInstance().getSettingsConfig().getConfig()
                    .getConfigurationSection("gui.quickbuy");
            IShopItem item = new ShopItem(section, "emptyslot.");
            for (int i : config.getIntegerList("quick-buy-slots")) {
                quickBuy.put(i, item);
                inventory.setItem(i, item.getItem());
            }



            loadQuickBuy(player.getQuickBuy().getQuickBuy());

        }
    }


    private void loadItems() {
        ConfigurationSection section = config.getConfigurationSection("items");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            String id = section.getString(key + ".id");
            if (id == null) continue;
            IShopItem item = ShopManager.getManager().getItemById(id);
            for (int slot : item.getSlots()) {
                items.put(slot, item);
            }

            for (int slot : items.keySet()) {
                inventory.setItem(slot, items.get(slot).getItem());
            }
        }

    }

    public void loadQuickBuy(Map<Integer, String> items) {
        for (int slot : items.keySet()) {
            IShopItem item = ShopManager.getManager().getItemById(items.get(slot));
            ItemStack stack = inventory.getItem(slot);
            if (stack.getType() != XMaterial.RED_STAINED_GLASS_PANE.parseMaterial()) continue;
            if (item == null) continue;
            quickBuy.put(slot, item);
            inventory.setItem(slot, item.getItem());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }


}
