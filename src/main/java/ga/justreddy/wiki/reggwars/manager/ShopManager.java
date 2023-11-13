package ga.justreddy.wiki.reggwars.manager;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShop;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.ShopType;
import ga.justreddy.wiki.reggwars.model.game.shop.Shop;
import ga.justreddy.wiki.reggwars.model.game.shop.ShopCategory;
import lombok.Getter;
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
        return manager == null ? manager = new ShopManager() : manager;
    }

    private final Map<String, IShop> shops;
    private final Map<String, IShopCategory> categories;
    private final File shopFolder;
    private final File categoryFolder;
    private final XMaterial material;

    private ShopManager() {
        this.shops = new HashMap<>();
        this.categories = new HashMap<>();
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
        FileConfiguration configuration = REggWars.getInstance().getSettingsConfig().getConfig();
        this.material = XMaterial.matchXMaterial(configuration.getString("gui.filler")).orElse(XMaterial.BLACK_STAINED_GLASS_PANE);
    }

    public void start() {
        loadShops();
        loadCategories();
        for (IShop shop : shops.values()) {
            ((Shop) shop).loadCategories();
        }
    }

    private void loadShops() {
        File[] files = shopFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (name.equals("main_shop.yml")) continue;
            if (name.equals("upgrade_shop.yml")) continue;
            if (!name.endsWith("shop.yml")) continue;
            name = name.replace(".yml", "");
            registerShop(name, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void loadCategories() {
        File[] files = categoryFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (!name.endsWith("category.yml")) continue;
            name = name.replace(".yml", "");
            registerCategory(name, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void registerShop(String name, FileConfiguration config) {
        if (shops.containsKey(name)) return;
        shops.put(name, new Shop(config));
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

}
