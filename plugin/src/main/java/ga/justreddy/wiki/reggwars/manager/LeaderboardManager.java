package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.Leaderboard;
import lombok.Getter;

import java.util.*;

/**
 * @author JustReddy
 */
@Getter
public class LeaderboardManager {

    private static LeaderboardManager instance;

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    private final Map<String, Leaderboard> leaderboards;
    private final Map<IGamePlayer, List<Leaderboard>> playerLeaderboards;

    private LeaderboardManager() {
        this.leaderboards = new HashMap<>();
        this.playerLeaderboards = new HashMap<>();
    }

    public void registerLeaderboard(Leaderboard leaderboard) {
        if (leaderboards.containsKey(leaderboard.getId())) {
            throw new IllegalArgumentException("Leaderboard with id " + leaderboard.getId() + " already exists");
        }
        leaderboards.put(leaderboard.getId(), leaderboard);
    }

    public void unregisterLeaderboard(Leaderboard leaderboard) {
        leaderboards.remove(leaderboard.getId());
    }

    public void addPlayerLeaderboard(IGamePlayer player, Leaderboard leaderboard) {
        List<Leaderboard> leaderboards = playerLeaderboards.getOrDefault(player, null);
        if (leaderboards == null) {
            leaderboards = new ArrayList<>();
            playerLeaderboards.put(player, leaderboards);
        }
        leaderboards.add(leaderboard);
    }

    public void removePlayerLeaderboard(IGamePlayer player, Leaderboard leaderboard) {
        List<Leaderboard> leaderboards = playerLeaderboards.getOrDefault(player, null);
        if (leaderboards == null) {
            return;
        }
        leaderboards.remove(leaderboard);
    }

    public Leaderboard getLeaderboard(String id) {
        return leaderboards.get(id);
    }

    public boolean exists(String id) {
        return leaderboards.containsKey(id);
    }

    public List<Leaderboard> getPlayerLeaderboards(IGamePlayer player) {
        return playerLeaderboards.getOrDefault(player, null);
    }

    public void destroy() {
        leaderboards.clear();
        playerLeaderboards.values().forEach(list -> {
            list.forEach(Leaderboard::destroy);
        });
        playerLeaderboards.clear();
    }



}
