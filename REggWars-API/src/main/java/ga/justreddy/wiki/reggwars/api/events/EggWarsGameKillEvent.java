package ga.justreddy.wiki.reggwars.api.events;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

/**
 * @author JustReddy
 */
public class EggWarsGameKillEvent extends EggWarsEvent {

    private final IGame game;
    private final IGamePlayer killer;
    private final IGamePlayer victim;
    private final boolean finalKill;

    public EggWarsGameKillEvent(IGame game, IGamePlayer killer, IGamePlayer victim, boolean finalKill) {
        this.game = game;
        this.killer = killer;
        this.victim = victim;
        this.finalKill = finalKill;
    }

    public IGame getGame() {
        return game;
    }

    public IGamePlayer getKiller() {
        return killer;
    }

    public IGamePlayer getVictim() {
        return victim;
    }

    public boolean isFinalKill() {
        return finalKill;
    }

}
