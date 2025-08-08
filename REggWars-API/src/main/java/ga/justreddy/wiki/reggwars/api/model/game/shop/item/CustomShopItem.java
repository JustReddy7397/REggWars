package ga.justreddy.wiki.reggwars.api.model.game.shop.item;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class represents a custom shop item in the game.
 * It provides methods to manage the item's upgrades and interactions.
 *
 * @author JustReddy
 */
public abstract class CustomShopItem {

    private final String id;
    private final ItemStack starterItem;
    private final Map<Integer, ItemStack> upgrades;
    private final Map<IGamePlayer, Integer> currentUpgrade;

    /**
     * Constructs a new CustomShopItem with the given id and starter item.
     *
     * @param id          the unique identifier for this item
     * @param starterItem the initial item stack for this item
     */
    public CustomShopItem(String id, ItemStack starterItem) {
        this.id = id;
        this.starterItem = starterItem;
        this.currentUpgrade = new HashMap<>();
        this.upgrades = new LinkedHashMap<>();
        addUpgrade(starterItem);
    }

    /**
     * Returns the unique identifier of this item.
     *
     * @return the id of this item
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the starter item stack of this item.
     *
     * @return the starter item stack
     */
    public ItemStack getStarterItem() {
        return starterItem;
    }

    /**
     * Checks if this item is upgradable.
     *
     * @return true if this item is upgradable, false otherwise
     */
    public abstract boolean isUpgradable();

    /**
     * Checks if this item is persistent after death.
     *
     * @return true if this item is persistent, false otherwise
     */
    public abstract boolean isPersistent();

    /**
     * Returns a list of upgrades for this item.
     *
     * @return a LinkedList of ItemStack representing the upgrades
     */
    public Map<Integer, ItemStack> getUpgrades() {
        return upgrades;
    }

    /**
     * Returns the upgrade at the specified level.
     *
     * @param level the level of the upgrade
     * @return the ItemStack representing the upgrade at the specified level
     */
    public ItemStack getUpgrade(int level) {
        if (level < 0 || upgrades.getOrDefault(level, null) == null) return null;
        return upgrades.get(level);
    }

    /**
     * Returns the next upgrade for the specified player.
     *
     * @param player the player to get the next upgrade for
     * @return the ItemStack representing the next upgrade, or null if there is no next upgrade
     */
    public ItemStack nextUpgrade(IGamePlayer player) {
        if (!isUpgradable()) return null;
        int currentLevel = currentUpgrade.getOrDefault(player, 0);
        ItemStack upgrade = getUpgrade(currentLevel);
        if (upgrade == null) return null;
        currentUpgrade.put(player, currentLevel + 1);
        return upgrade;
    }

    /**
     * Returns the previous upgrade for the specified player.
     *
     * @param player the player to get the previous upgrade for
     * @return the ItemStack representing the previous upgrade, or null if there is no previous upgrade
     */
    public ItemStack previousUpgrade(IGamePlayer player) {
        if (!isUpgradable()) return null;
        int currentLevel = currentUpgrade.getOrDefault(player, 0);
        if (currentLevel == 0) return getUpgrade(0);
        ItemStack upgrade = getUpgrade(currentLevel - 1);
        if (upgrade == null) return null;
        currentUpgrade.put(player, currentLevel - 1);
        return upgrade;
    }

    /**
     * Returns the current upgrade for the specified player.
     * @param player the player to get the current upgrade for
     * @return the ItemStack representing the current upgrade
     */
    public ItemStack getCurrentUpgrade(IGamePlayer player) {
        return getUpgrade(currentUpgrade.getOrDefault(player, 0));
    }

    /**
     * Removes the specified player from the current upgrade map.
     *
     * @param player the player to remove
     */
    public void remove(IGamePlayer player) {
        currentUpgrade.remove(player);
    }

    /**
     * Defines the behavior of this item when the player dies.
     *
     * @param player the player who died
     */
    public abstract void onDeath(IGamePlayer player);

    /**
     * Defines the behavior of this item when the player respawns.
     * @param player the player who respawns
     */
    public abstract void onRespawn(IGamePlayer player);

    /**
     * Defines the behavior of this item when clicked.
     *
     * @param player the player who clicked the item
     * @param action the action performed by the player
     */
    public abstract void onClick(IGamePlayer player, ClickAction action);

    /**
     * Adds an upgrade to this item at the specified level.
     * @param level the level of the upgrade
     * @param upgrade the ItemStack representing the upgrade
     */
    public void addUpgrade(int level, ItemStack upgrade) {
        upgrades.put(level, upgrade);
    }

    /**
     * Adds an upgrade to this item at the next level.
     * @param upgrade the ItemStack representing the upgrade
     */
    public void addUpgrade(ItemStack upgrade) {
        upgrades.put(upgrades.size(), upgrade);
    }

    /**
     * Removes the upgrade at the specified level.
     * @param level the level of the upgrade to remove
     */
    public void removeUpgrade(int level) {
        upgrades.remove(level);
    }

}
