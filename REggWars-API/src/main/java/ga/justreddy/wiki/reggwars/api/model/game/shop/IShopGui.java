package ga.justreddy.wiki.reggwars.api.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IShopGui extends Cloneable {

    List<IShopItem> getItems();

    void onClick(IGamePlayer player, int slot, boolean shift);

    void open(IGamePlayer player);

}
