package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.Leaderboard;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.PerPlayerLeaderboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class LeaderboardManager {

    private static LeaderboardManager instance;

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    private final List<String> leaderboardIds;
    private final Map<String, Leaderboard> leaderboards;
    private final Map<IGamePlayer, List<PerPlayerLeaderboard>> playerLeaderboards;

    public LeaderboardManager() {
        this.leaderboardIds = new ArrayList<>();
        this.leaderboards = new HashMap<>();
        this.playerLeaderboards = new HashMap<>();
    }



}
