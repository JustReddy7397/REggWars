package ga.justreddy.wiki.reggwars.model.replays.advancedreplay;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameKillEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameRestartEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameStartEvent;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.model.replays.GameReplayCache;
import ga.justreddy.wiki.reggwars.model.replays.PlayerInfo;
import ga.justreddy.wiki.reggwars.model.replays.ReplayAdapter;
import ga.justreddy.wiki.reggwars.model.replays.StartQueue;
import me.jumper251.replay.api.ReplayAPI;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.utils.ReplayManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

/**
 * @author JustReddy
 */
public class AdvancedReplayAdapter extends ReplayAdapter {

    private final Map<UUID, List<GameReplayCache>> playerReplayCache;
    private final Map<String, GameReplayCache> replayCacheIds;
    private final Map<String, GameReplayCache> ongoingRecordings;
    private final Map<IGamePlayer, StartQueue> startQueue;

    public AdvancedReplayAdapter() {
        this.playerReplayCache = new HashMap<>();
        this.replayCacheIds = new HashMap<>();
        this.ongoingRecordings = new HashMap<>();
        this.startQueue = new HashMap<>();
    }

    @Override
    public void startRecording(EggWarsGameStartEvent event) {
        List<PlayerInfo> playerInfos = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        IGame game = event.getGame();
        for (IGamePlayer player : game.getAlivePlayers()) {
            players.add(player.getPlayer());
            PlayerInfo info = new PlayerInfo(
                    player.getName(),
                    player.getUniqueId(),
                    "",
                    "",
                    "",
                    ""
            ); // TODO

            playerInfos.add(info);

        }

        String name = game.getDisplayName().trim() + "_" + UUID.randomUUID().toString().substring(0, 6);
        ReplayAPI.getInstance().recordReplay(name,
                Bukkit.getConsoleSender(), players);

        GameReplayCache cache = new GameReplayCache(
                playerInfos,
                game.getDisplayName(),
                game.getGameMode().name(),
                System.currentTimeMillis(),
                name
        );

        ongoingRecordings.put(game.getName(), cache);
    }

    @Override
    public void stopRecording(EggWarsGameRestartEvent event) {
        IGame game = event.getGame();
        if (!ongoingRecordings.containsKey(game.getName())) return;
        GameReplayCache cache = ongoingRecordings.get(game.getName());
        List<String> spectators = new ArrayList<>();
        for (IGamePlayer spectator : event.getGame().getDeadPlayers()) {
            spectators.add(spectator.getName());
        }

        cache.setSpectators(spectators);
        Replay replay = ReplayManager.activeReplays.get(cache.getReplayName());
        cache.setTotalDuration(replay.getRecorder().getCurrentTick() / 20);
        ongoingRecordings.remove(game.getName());
        ReplayAPI.getInstance().stopReplay(cache.getReplayName(), true);
        Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
            // TODO update db
            for (UUID uuid : cache.getPlayersWithUUIDs().keySet()) {
                if (playerReplayCache.containsKey(uuid)) {
                    playerReplayCache.get(uuid).add(cache);
                } else {
                    playerReplayCache.put(uuid, new ArrayList<>(Collections.singletonList(cache)));
                }
            }
            replayCacheIds.put(cache.getReplayName(), cache);
        });

    }

    @Override
    public Map<UUID, List<GameReplayCache>> getPlayerReplayCache() {
        return playerReplayCache;
    }

    @Override
    public Map<String, GameReplayCache> getReplayCacheIds() {
        return replayCacheIds;
    }

    @Override
    public Map<String, GameReplayCache> getOnGoingRecordings() {
        return ongoingRecordings;
    }

    @Override
    public Map<IGamePlayer, StartQueue> getStartQueues() {
        return startQueue;
    }

    @EventHandler
    public void onGameStart(EggWarsGameStartEvent event) {
        startRecording(event);
    }

    @EventHandler
    public void onGameRestart(EggWarsGameRestartEvent event) {
        stopRecording(event);
    }

    @EventHandler
    public void onGameKill(EggWarsGameKillEvent event) {
        IGame game = event.getGame();
        IGamePlayer victim = event.getVictim();
        if (!ongoingRecordings.containsKey(game.getName())) return;
        GameReplayCache cache = ongoingRecordings.get(game.getName());
        Replay replay = ReplayManager.activeReplays.get(cache.getReplayName());
        Map<UUID, List<Integer>> deathTimes = cache.getUUIDsWithDeathTimes();
        if (deathTimes.containsKey(victim.getUniqueId())) {
            deathTimes.get(victim.getUniqueId())
                    .add(replay.getRecorder().getCurrentTick() / 20);
        } else {
            deathTimes.put(victim.getUniqueId(),
                    new ArrayList<>(Collections.singletonList(replay.getRecorder().getCurrentTick() / 20)));
        }
    }

}
