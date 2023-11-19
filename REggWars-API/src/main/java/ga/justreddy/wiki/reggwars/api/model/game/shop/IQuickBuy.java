package ga.justreddy.wiki.reggwars.api.model.game.shop;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author JustReddy
 */
public interface IQuickBuy {

    Map<Integer, IShopItem> getItems();

}
