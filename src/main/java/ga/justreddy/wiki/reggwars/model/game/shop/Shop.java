package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShop;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.ShopType;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.processing.Filer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
public class Shop implements IShop {

    private final List<IShopCategory> categories;
    private final ShopType type;
    private final IShopGui main;
    private final FileConfiguration config;

    public Shop(FileConfiguration config) {
        this.config = config;
        this.categories = new ArrayList<>();
        this.type = ShopType.valueOf(config.getString("type"));
        this.main = new ShopGui(config);
    }

    @Override
    public ShopType getShopType() {
        return type;
    }

    @Override
    public List<IShopCategory> getCategories() {
        return null;
    }

    @Override
    public void open(IGamePlayer player) {
        main.open(player);
    }

    @Override
    public void spawn(Location location) {
        REggWars.getInstance().getNms().spawnVillager(location);
    }

    public void loadCategories() {
        // TODO load categories
        for (String category : config.getStringList("categories")) {
            IShopCategory shopCategory = ShopManager.getManager().getCategoryById(category);
            if (shopCategory == null) continue;
            categories.add(shopCategory);
        }

    }

}
