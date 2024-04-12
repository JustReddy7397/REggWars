package ga.justreddy.wiki.reggwars.tasks;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.Leaderboard;
import ga.justreddy.wiki.reggwars.api.model.leaderboard.PerPlayerLeaderboard;
import ga.justreddy.wiki.reggwars.manager.LeaderboardManager;

import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class LeaderboardTask implements Runnable {

    // Hehe

    @Override
    public void run() {
        for (Map.Entry<IGamePlayer, List<Leaderboard>> entry : LeaderboardManager.getInstance().getPlayerLeaderboards().entrySet()) {
            List<Leaderboard> leaderboards = entry.getValue();
            for (Leaderboard leaderboard : leaderboards) {
                if (leaderboard instanceof PerPlayerLeaderboard) {
                    ((PerPlayerLeaderboard) leaderboard).update(entry.getKey());
                } else {
                    leaderboard.update();
                }
            }
        }
    }
}
