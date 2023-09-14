package ga.justreddy.wiki.reggwars.model.replays;

import ga.justreddy.wiki.reggwars.api.events.EggWarsGameRestartEvent;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameStartEvent;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public abstract class ReplayAdapter implements Listener {

    public abstract void startRecording(EggWarsGameStartEvent event);

    public abstract void stopRecording(EggWarsGameRestartEvent event);

    public abstract Map<UUID, List<GameReplayCache>> getPlayerReplayCache();

    public abstract Map<String, GameReplayCache> getReplayCacheIds();

    public abstract Map<String, GameReplayCache> getOnGoingRecordings();

    public abstract Map<IGamePlayer, StartQueue> getStartQueues();


}
