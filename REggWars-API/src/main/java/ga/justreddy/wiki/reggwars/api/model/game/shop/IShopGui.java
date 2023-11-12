package ga.justreddy.wiki.reggwars.api.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IShopGui {

    List<IShopItem> getItems();

    void onClick(IGamePlayer player, int slot);

    void open(IGamePlayer player);

}
