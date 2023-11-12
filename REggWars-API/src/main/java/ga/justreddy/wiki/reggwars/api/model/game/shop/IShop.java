package ga.justreddy.wiki.reggwars.api.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import org.bukkit.Location;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IShop {

    ShopType getShopType();

    List<IShopCategory> getCategories();

    void open(IGamePlayer player);

    void spawn(Location location);

}
