package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
public class ShopCategory implements IShopCategory {

    private final String category;
    private final List<IShopItem> items;
    private final FileConfiguration configuration;
    private IShopGui gui;


    public ShopCategory(FileConfiguration configuration) {
        this.configuration = configuration;
        this.category = configuration.getString("category");
        this.items = new ArrayList<>();
        loadItems(configuration);
    }

    public void setGui(IShopGui gui) {
        this.gui = gui;
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public List<IShopItem> getItems() {
        return items;
    }

    @Override
    public IShopGui getGui() {
        return gui;
    }

    private void loadItems(FileConfiguration configuration) {
        ConfigurationSection items = configuration.getConfigurationSection("items");
        for (String key : items.getKeys(false)) {
            IShopItem item = new ShopItem(items.getConfigurationSection(key));
            this.items.add(item);
        }
    }

}
