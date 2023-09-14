package ga.justreddy.wiki.reggwars.model.replays;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author JustReddy
 */
@Getter
@Setter
public class GameReplayCache {

    private final Map<String, PlayerInfo> playersWithNames = new HashMap<>();
    private final Map<UUID, PlayerInfo> playersWithUUIDs = new HashMap<>();
    private Map<UUID, List<Integer>> UUIDsWithDeathTimes;
    private List<String> spectators;
    //SS
    private int totalDuration;
    private String mapName = "";
    private long date;
    private String gameMode;
    private String replayName;

    public GameReplayCache(List<PlayerInfo> players,
                           String mapName, String gameMode, long date, String replayName){
        this.date = date;
        this.gameMode = gameMode;
        this.mapName = mapName;
        for(PlayerInfo player : players){
            playersWithUUIDs.put(player.getUuid(), player);
            playersWithNames.put(player.getName(), player);
        }
        this.UUIDsWithDeathTimes = new HashMap<>();
        this.spectators = new ArrayList<>();
        this.replayName = replayName;
    }
    public String getPlayerName(String uuid){
        UUID uuid1 = UUID.fromString(uuid);
        if (playersWithUUIDs.containsKey(uuid1)){
            return playersWithUUIDs.get(uuid1).getName();
        }
        return "";
    }

    public PlayerInfo getPlayerInfo(UUID uuid){
        return playersWithUUIDs.get(uuid);
    }

    public PlayerInfo getPlayerInfo(String name) {
        return playersWithNames.get(name);
    }



}
