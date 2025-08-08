package ga.justreddy.wiki.reggwars.model.game.items;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.ClickAction;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.CustomShopItem;
import org.bukkit.inventory.ItemStack;

/**
 * @author JustReddy
 */
public class SheerItem extends CustomShopItem {

    /**
     * Constructs a new CustomShopItem with the given id and starter item.
     *
     * @param id          the unique identifier for this item
     * @param starterItem the initial item stack for this item
     */
    public SheerItem() {
        super("sheer", new ItemStack(XMaterial.SHEARS.parseMaterial()));
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public void onDeath(IGamePlayer player) {

    }

    @Override
    public void onRespawn(IGamePlayer player) {

    }

    @Override
    public void onClick(IGamePlayer player, ClickAction action) {

    }
}
