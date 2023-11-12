package ga.justreddy.wiki.reggwars.api.model.game.shop;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IShopCategory {

    String getCategory();

    List<IShopItem> getItems();

    IShopGui getGui();

}
