package ga.justreddy.wiki.reggwars.manager;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.shop.*;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.CustomShopItem;
import ga.justreddy.wiki.reggwars.model.game.shop.Shop;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopCategory;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopGui;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopItem;
import ga.justreddy.wiki.reggwars.model.game.test.CustomTestItem;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author JustReddy
 */
@Getter
public class ShopManager {

    private static ShopManager manager;

    public static ShopManager getManager() {
        return manager;
    }

    private final Map<String, IShop> shops;
    private final Map<String, IShopCategory> categories;
    private final Map<String, IShopItem> items;
    private final Map<String, CustomShopItem> customShopItems;
    private final File shopFolder;
    private final File categoryFolder;
    private final XMaterial material;

    public ShopManager() {
        manager = this;
        FileConfiguration configuration = REggWars.getInstance().getSettingsConfig().getConfig();
        this.material = XMaterial.matchXMaterial(configuration.getString("gui.filler")).orElse(XMaterial.BLACK_STAINED_GLASS_PANE);
        this.customShopItems = new HashMap<>();
        this.shops = new HashMap<>();
        this.categories = new HashMap<>();
        this.items = new HashMap<>();
        registerCustomShopItem(new CustomTestItem());
        this.shopFolder = new File(REggWars.getInstance().getDataFolder(), "shops");
        if (!shopFolder.exists()) shopFolder.mkdirs();
        this.categoryFolder = new File(REggWars.getInstance().getDataFolder() + "/shops", "categories");
        File file;
        if (!(file = new File(shopFolder.getAbsolutePath() + "/main_shop.yml")).exists()) {
            REggWars.getInstance().saveResource("shops/main_shop.yml", false);
        }

        registerShop("main", YamlConfiguration.loadConfiguration(file));


        if (!(file = new File(shopFolder.getAbsolutePath() + "/upgrade_shop.yml")).exists()) {
            REggWars.getInstance().saveResource("shops/upgrade_shop.yml", false);
        }

        registerShop("upgrade", YamlConfiguration.loadConfiguration(file));

        if (!categoryFolder.exists()) categoryFolder.mkdirs();
    }

    public void start() {
        loadShops();
        loadCategories();

        for (IShop shop : shops.values()) {
            ((Shop) shop).loadCategories();
        }

        for (IShopCategory category : categories.values()) {
            for (IShopItem item : category.getItems()) {
                if (items.containsKey(item.getId())) continue;
                items.put(item.getId(), item);
            }
        }


        for (IShopCategory category : categories.values()) {
            ShopCategory shopCategory = (ShopCategory) category;
            shopCategory.setGui(new ShopGui(shopCategory.getConfig()));
        }
    }

    private void loadShops() {
        File[] files = shopFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (name.equals("main_shop.yml")) continue;
            if (name.equals("upgrade_shop.yml")) continue;
            if (!name.endsWith("_shop.yml")) continue;
            name = name.replace("_shop.yml", "");
            registerShop(name, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void loadCategories() {
        File[] files = categoryFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (!name.endsWith("_category.yml")) continue;
            name = name.replace("_category.yml", "");
            registerCategory(name, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void registerShop(String name, FileConfiguration config) {
        if (shops.containsKey(name)) return;
        shops.put(name, new Shop(config));
        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) return;
        for (String key : items.getKeys(false)) {
            IShopItem item = new ShopItem(items.getConfigurationSection(key));
            this.items.put(item.getId(), item);
        }
    }

    private void registerCategory(String name, FileConfiguration config) {
        if (categories.containsKey(name)) return;
        categories.put(name, new ShopCategory(config));
    }

    public IShop getShopById(String name) {
        return shops.getOrDefault(name, null);
    }

    public IShop getShopByType(ShopType shopType) {
        for (IShop shop : shops.values()) {
            if (Objects.equals(shop.getShopType().getId(), shopType.getId())) return shop;
        }
        return null;
    }

    public IShopCategory getCategoryById(String name) {
        return categories.getOrDefault(name, null);
    }

    public IShopItem getItemById(String id) {
         return items.getOrDefault(id, null);
    }

    public void registerCustomShopItem(CustomShopItem item) {
        if (customShopItems.containsKey(item.getId())) {
            ChatUtil.sendConsole("&cCustom shop item with id " + item.getId() + " already exists!");
            return;
        }
        customShopItems.put(item.getId(), item);
    }

    public CustomShopItem getCustomItemById(String customItem) {
        return customShopItems.getOrDefault(customItem, null);
    }
}
