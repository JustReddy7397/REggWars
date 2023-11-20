package ga.justreddy.wiki.reggwars.api.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author JustReddy
 */
public interface IShopItem {

    String getId();

    ItemStack getItem();

    IShopPrice getPrice();

    int getAmount();

    int getSlot();

    int[] getSlots();

    boolean shouldColor();

    boolean isDummy();

    boolean canQuickBuy();

    void give(IShopGui gui, IGamePlayer player, boolean shift);

}
