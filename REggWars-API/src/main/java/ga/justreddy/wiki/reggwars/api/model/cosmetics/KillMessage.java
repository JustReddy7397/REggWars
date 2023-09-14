package ga.justreddy.wiki.reggwars.api.model.cosmetics;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public abstract class KillMessage extends Cosmetic {

    protected KillMessage(int id, String subname, int cost, String permission, CosmeticRarity rarity) {
        super(id, subname, cost, permission, rarity);
    }

    public abstract void sendProjectileMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal);

    public abstract void sendMeleeMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal);

    public abstract void sendVoidMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal);

    public abstract void sendFallMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal);

    public abstract void sendEggBreakMessage(IGame game, IGamePlayer killer, IGamePlayer victim);


}
