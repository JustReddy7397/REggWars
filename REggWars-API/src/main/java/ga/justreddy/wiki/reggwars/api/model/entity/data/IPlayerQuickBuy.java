package ga.justreddy.wiki.reggwars.api.model.entity.data;

import java.util.Map;

/**
 * @author JustReddy
 */
public interface IPlayerQuickBuy {

    /**
     * Add item to quick buy
     * @param slot The slot the item should be put in
     * @param item The item id
     */
    void add(int slot, String item);

    /**
     * Get item from quick buy
     * @param slot The slot the item is in
     * @return The item id
     */
    String get(int slot);

    /**
     * Get slot from quick buy
     * @param id The item id
     * @return The slot the item is in
     */
    int get(String id);

    /**
     * Remove item from quick buy
     * @param slot The slot the item is in
     */
    void remove(int slot);

    /**
     * Get quick buy items
     * @return The quick buy items
     */
    Map<Integer, String> getQuickBuy();

}
