package ga.justreddy.wiki.reggwars.model.cosmetics.dances;

import ga.justreddy.wiki.reggwars.api.model.cosmetics.CosmeticRarity;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;

/**
 * @author JustReddy
 */
public class DefaultDance extends VictoryDance {

    protected DefaultDance(int id, String subname, int cost, String permission, CosmeticRarity rarity) {
        super(id, subname, cost, permission, rarity);
    }

    @Override
    public void start(IGamePlayer winner) {

    }

    @Override
    public void stop(IGamePlayer winner) {

    }
}
