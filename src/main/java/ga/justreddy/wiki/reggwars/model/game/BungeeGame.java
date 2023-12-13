package ga.justreddy.wiki.reggwars.model.game;

import ga.justreddy.wiki.reggwars.api.model.game.GameMode;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@ToString
@Getter
public class BungeeGame implements Serializable {

    private final String name;
    private final String server;
    private GameState state = GameState.WAITING;
    private GameMode gameMode;
    private List<String> players = new ArrayList<>();
    private final int maxPlayers;

    public BungeeGame(String name, String server, int maxPlayers, GameMode gameMode) {
        this.name = name;
        this.server = server;
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;
    }

    public BungeeGame(String name, String server, int maxPlayers, GameState state, GameMode gameMode, List<String> players) {
        this.name = name;
        this.server = server;
        this.maxPlayers = maxPlayers;
        this.state = state;
        this.gameMode = gameMode;
        this.players = players;
    }

    public boolean isGameState(GameState gameState) {
        return state == gameState;
    }

    public boolean isGameMode(GameMode gameMode) {
        return this.gameMode == gameMode;
    }

}
