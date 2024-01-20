package ga.justreddy.wiki.reggwars.model.game.shop;

import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopPrice;
import org.bukkit.Material;

/**
 * @author JustReddy
 */
public class ShopPrice implements IShopPrice {

    private final Material material;
    private final int price;

    public ShopPrice(Material material, int price) {
        this.material = material;
        this.price = price;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public int getPrice() {
        return price;
    }
}
