package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.model.game.Game;
import lombok.Getter;

/**
 * @author JustReddy
 */
@Getter
public class GamePhaseManager {

    private final Game game;

    private GamePhase currentPhase;

    public GamePhaseManager(Game game) {
        this.game = game;
    }

    public boolean isInPhase(Class<? extends GamePhase> phaseClass) {
        return currentPhase.getClass().equals(phaseClass);
    }

    public void nextPhase() {
        GamePhase phase = currentPhase.getNextPhase();
        if (phase == null) return;
        setPhase(phase);
    }

    public void setPhase(GamePhase phase) {
        this.currentPhase = phase;
        this.currentPhase.onEnable(game);
    }

    public void onTick() {
        if (currentPhase == null) return;
        currentPhase.onTick(game);
    }

}
