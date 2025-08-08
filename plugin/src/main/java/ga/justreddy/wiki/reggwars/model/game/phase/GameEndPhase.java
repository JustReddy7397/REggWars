package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.quests.QuestType;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;

import javax.annotation.Nullable;

/**
 * @author JustReddy
 */
public class GameEndPhase extends GamePhase {

    private final @Nullable IGameTeam winningTeam;

    public GameEndPhase(@Nullable IGameTeam winningTeam) {
        this.winningTeam = winningTeam;
    }

    @Override
    public void onEnable(Game game) {
        game.setGameState(GameState.ENDING);
        super.onEnable(game);
        game.getGenerators().forEach(IGenerator::destroy);
        for (IGamePlayer player : game.getActualPlayers()) {
            player.getQuests().update(player, QuestType.PLAY_GAMES);
        }
        if (game.getEndTimer().isStarted()) {
            game.getEndTimer().stop();
        }
        game.getEndTimer().start();
        game.sendLegacyMessage("Thanks for playing bitches");
    }

    @Override
    public void onTick(Game game) {
        if (game.getEndTimer().isStarted()) {
            game.getEndTimer().stop();
            game.goToNextPhase();
        }
    }

    @Override
    public void onDisable(Game game) {

    }

    @Override
    public GamePhase getNextPhase() {
        return new GameRestartingPhase();
    }
}
