package ga.justreddy.wiki.reggwars.model.game.test;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.ClickAction;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.CustomShopItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

/**
 * @author JustReddy
 */
public class CustomTestItem extends CustomShopItem {

    public CustomTestItem() {
        super("test", new ItemStack(Material.APPLE));
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public LinkedList<ItemStack> getUpgrades() {
        return new LinkedList<>();
    }

    @Override
    public void onDeath(IGamePlayer player) {
        // Nothing :(
    }

    @Override
    public void onClick(IGamePlayer player, ClickAction action) {
    }
}
