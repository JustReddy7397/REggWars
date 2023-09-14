package ga.justreddy.wiki.reggwars.model.replays.xreplay;

import ga.justreddy.wiki.reggwars.api.events.EggWarsGameRestartEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameStartEvent;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.replays.GameReplayCache;
import ga.justreddy.wiki.reggwars.model.replays.ReplayAdapter;
import ga.justreddy.wiki.reggwars.model.replays.StartQueue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class XReplayAdapter extends ReplayAdapter {
    @Override
    public void startRecording(EggWarsGameStartEvent event) {
        // TODO CANT ADD THIS CAUSE XREPLAY SUCKS!
    }

    @Override
    public void stopRecording(EggWarsGameRestartEvent event) {

    }

    @Override
    public Map<UUID, List<GameReplayCache>> getPlayerReplayCache() {
        return null;
    }

    @Override
    public Map<String, GameReplayCache> getReplayCacheIds() {
        return null;
    }

    @Override
    public Map<String, GameReplayCache> getOnGoingRecordings() {
        return null;
    }

    @Override
    public Map<IGamePlayer, StartQueue> getStartQueues() {
        return null;
    }
}
