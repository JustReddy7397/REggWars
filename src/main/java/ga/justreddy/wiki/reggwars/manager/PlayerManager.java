package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class PlayerManager {

    private static PlayerManager manager;

    public static PlayerManager getManager() {
        return manager == null ? manager = new PlayerManager() : manager;
    }

    private final Map<UUID, IGamePlayer> players;

    public PlayerManager() {
        this.players = new HashMap<>();
    }

    public IGamePlayer addGamePlayer(UUID uuid, String name) {
        IGamePlayer gamePlayer = getGamePlayer(uuid);
        if (gamePlayer == null) {
            players.put(uuid, gamePlayer = new GamePlayer(uuid, name));
        }
        return gamePlayer;
    }

    public IGamePlayer getGamePlayer(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    public void removeGamePlayer(UUID uuid) {
        players.remove(uuid);
    }

}
