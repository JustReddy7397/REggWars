package ga.justreddy.wiki.reggwars.api.model.entity.data;

import java.util.Map;

/**
 * @author JustReddy
 */
public interface IPlayerQuickBuy {

    void add(int slot, String item);

    String get(int slot);

    void remove(int slot);

    Map<Integer, String> getQuickBuy();

}
