package ga.justreddy.wiki.reggwars.model.entity.data;

import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerQuickBuy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author JustReddy
 */
public class PlayerQuickBuy implements IPlayerQuickBuy {

    private final Map<Integer, String> quickBuy;

    public PlayerQuickBuy() {
        this.quickBuy = new HashMap<>();
    }


    @Override
    public void add(int slot, String item) {
        if (quickBuy.containsKey(slot)) remove(slot);
        quickBuy.put(slot, item);
    }

    @Override
    public String get(int slot) {
        return quickBuy.getOrDefault(slot, null);
    }

    @Override
    public int get(String id) {
        Map.Entry<Integer, String> set =
                quickBuy.entrySet()
                        .stream().filter(entry -> entry.getValue().equals(id)).findFirst()
                        .orElse(null);
        if (set == null) return -1;
        return set.getKey();
    }

    @Override
    public void remove(int slot) {
        quickBuy.remove(slot);
    }

    @Override
    public Map<Integer, String> getQuickBuy() {
        return quickBuy;
    }
}
