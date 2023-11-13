package ga.justreddy.wiki.reggwars.api.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author JustReddy
 */
public interface IShopItem {

    ItemStack getItem();

    IShopPrice getPrice();

    int getAmount();

    int getSlot();

    int[] getSlots();

    boolean shouldColor();

    boolean isBuyable();

    void give(IGamePlayer player);

}
