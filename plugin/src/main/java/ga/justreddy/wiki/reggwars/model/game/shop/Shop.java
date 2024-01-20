package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.shop.*;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.processing.Filer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class Shop implements IShop {

    private final List<IShopCategory> categories;
    private final Map<Integer, IShopItem> quickBuy;
    private final ShopType type;
    private final FileConfiguration config;

    public Shop(FileConfiguration config) {
        this.config = config;
        this.categories = new ArrayList<>();
        this.quickBuy = new HashMap<>();
        this.type = ShopType.valueOf(config.getString("type"));
    }

    @Override
    public ShopType getShopType() {
        return type;
    }

    @Override
    public List<IShopCategory> getCategories() {
        return categories;
    }

    @Override
    public void open(IGamePlayer player) {
        IShopGui cloned = new ShopGui(config);
        cloned.open(player);

    }

    private void loadQuickBuy(IGamePlayer player, ShopGui gui) {
        // TODO load quick buy
    }

    @Override
    public void spawn(Location location) {
        REggWars.getInstance().getNms().spawnVillager(location);
    }

    public void loadCategories() {
        for (String category : config.getStringList("categories")) {
            IShopCategory shopCategory = ShopManager.getManager().getCategoryById(category);
            if (shopCategory == null) continue;
            categories.add(shopCategory);
        }

    }

}
