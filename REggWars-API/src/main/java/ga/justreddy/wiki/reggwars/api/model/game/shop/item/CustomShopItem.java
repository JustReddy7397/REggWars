package ga.justreddy.wiki.reggwars.api.model.game.shop.item;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author JustReddy
 */
public abstract class CustomShopItem {

    private final String id;
    private final ItemStack toGive;
    private final Map<IGamePlayer, Integer> currentUpgrade;

    public CustomShopItem(String id, ItemStack toGive) {
        this.id = id;
        this.toGive = toGive;
        this.currentUpgrade = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public ItemStack getToGive() {
        return toGive;
    }

    public abstract boolean isUpgradable();

    public abstract boolean isPersistent();

    public abstract LinkedList<ItemStack> getUpgrades();

    public ItemStack getUpgrade(int level) {
        return getUpgrades().get(level);
    }

    public ItemStack nextUpgrade(IGamePlayer player) {
        if (!isUpgradable()) return null;
        int currentLevel = currentUpgrade.getOrDefault(player, 0);
        ItemStack upgrade = getUpgrade(currentLevel);
        if (upgrade == null) return null;
        currentUpgrade.put(player, currentLevel + 1);
        return upgrade;
    }

    public ItemStack previousUpgrade(IGamePlayer player) {
        if (!isUpgradable()) return null;
        int currentLevel = currentUpgrade.getOrDefault(player, 0);
        ItemStack upgrade = getUpgrade(currentLevel);
        if (upgrade == null) return null;
        currentUpgrade.put(player, currentLevel - 1);
        return upgrade;
    }

    public void remove(IGamePlayer player) {
        currentUpgrade.remove(player);
    }

    public abstract void onDeath(IGamePlayer player);

    public abstract void onClick(IGamePlayer player, ClickAction action);

}
