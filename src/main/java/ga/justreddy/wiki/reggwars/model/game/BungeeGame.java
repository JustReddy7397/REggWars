package ga.justreddy.wiki.reggwars.model.game;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@Getter
public class BungeeGame implements Serializable {

    private final String name;
    private final String server;
    private GameState state = GameState.WAITING;
    private List<String> players = new ArrayList<>();
    private final int maxPlayers;

    public BungeeGame(String name, String server, int maxPlayers) {
        this.name = name;
        this.server = server;
        this.maxPlayers = maxPlayers;
    }

    public BungeeGame(String name, String server, int maxPlayers, GameState state, List<String> players) {
        this.name = name;
        this.server = server;
        this.maxPlayers = maxPlayers;
        this.state = state;
        this.players = players;
    }

    @Override
    public String toString() {
        return "BungeeGame{" +
                "name='" + name + '\'' +
                ", server='" + server + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", state=" + state +
                ", players=" + players +
                '}';
    }

    public boolean isGameState(GameState gameState) {
        return state == gameState;
    }
}
