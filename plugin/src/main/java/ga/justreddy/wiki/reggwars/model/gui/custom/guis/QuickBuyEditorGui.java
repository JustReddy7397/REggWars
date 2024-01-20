package ga.justreddy.wiki.reggwars.model.gui.custom.guis;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerQuickBuy;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopItem;
import ga.justreddy.wiki.reggwars.model.gui.custom.Gui;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class QuickBuyEditorGui extends Gui {

    private final FileConfiguration config;
    private final Map<Integer, IShopItem> quickBuy;
    private final IShopItem item;
    private final IShopGui previous;

    public QuickBuyEditorGui(IShopGui previous, IShopItem item) {
        this.previous = previous;
        this.item = item;
        File file = new File(ShopManager.getManager().getShopFolder().getAbsolutePath(), "main_shop.yml");
        config = YamlConfiguration.loadConfiguration(file);
        this.quickBuy = new HashMap<>();
    }

    @Override
    public String name() {
        return "QuickBuy Editor";
    }

    @Override
    public int size() {
        return 54;
    }

    @Override
    public void inventoryClick(InventoryClickEvent event) {
        IGamePlayer player = PlayerManager.getManager().getGamePlayer(event.getWhoClicked().getUniqueId());
        int slot = event.getRawSlot();
        if (!quickBuy.containsKey(slot)) return;
        IPlayerQuickBuy playerQuickBuy = player.getQuickBuy();
        if (playerQuickBuy.get(item.getId()) != -1) {
            playerQuickBuy.remove(playerQuickBuy.get(item.getId()));
        }
        playerQuickBuy.add(slot, item.getId());
        previous.open(player);
    }

    @Override
    public void setMenuItems(IGamePlayer player) {
        loadItems();
        loadEmptyQuickBuy();
        loadQuickBuy(player.getQuickBuy().getQuickBuy());
    }


    private void loadItems() {
        ConfigurationSection section = config.getConfigurationSection("items");
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            String id = section.getString(key + ".id");
            if (id == null) continue;
            IShopItem item = ShopManager.getManager().getItemById(id);
            for (int slots : item.getSlots()) {
                setItem(slots, item.getItem());
            }
        }
    }

    private void loadQuickBuy(Map<Integer, String> items) {
        for (int slot : items.keySet()) {
            IShopItem item = ShopManager.getManager().getItemById(items.get(slot));
            ItemStack stack = inventory.getItem(slot);
            if (stack.getType() != XMaterial.RED_STAINED_GLASS_PANE.parseMaterial()) continue;
            if (item == null) continue;
            quickBuy.put(slot, item);
            inventory.setItem(slot, item.getItem());
        }
    }

    private void loadEmptyQuickBuy() {
        if (config.isSet("quick-buy-slots")) {
            ConfigurationSection section = REggWars.getInstance().getSettingsConfig().getConfig()
                    .getConfigurationSection("gui.quickbuy");
            IShopItem item = new ShopItem(section, "emptyslot.");
            for (int i : config.getIntegerList("quick-buy-slots")) {
                quickBuy.put(i, item);
                inventory.setItem(i, item.getItem());
            }
        }
    }


}
