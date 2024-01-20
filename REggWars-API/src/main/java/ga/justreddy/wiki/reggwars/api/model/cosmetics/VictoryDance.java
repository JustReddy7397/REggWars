package ga.justreddy.wiki.reggwars.api.model.cosmetics;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;

/**
 * @author JustReddy
 */
public abstract class VictoryDance extends Cosmetic {

    public VictoryDance(int id, String subname, int cost, String permission, CosmeticRarity rarity) {
        super(id, subname, cost, permission, rarity);
    }

    public abstract void start(IGamePlayer winner);

    public abstract void stop(IGamePlayer winner);

}
