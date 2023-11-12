package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
public class ShopCategory implements IShopCategory {

    private final String category;
    private final List<IShopItem> items;
    private final IShopGui gui;

    public ShopCategory(FileConfiguration configuration) {
        this.category = configuration.getString("category");
        this.items = new ArrayList<>();
        loadItems();
        this.gui = new ShopGui(configuration);
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public List<IShopItem> getItems() {
        return items
    }

    @Override
    public IShopGui getGui() {
        return null;
    }
}
