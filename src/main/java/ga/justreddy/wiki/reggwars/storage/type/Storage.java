package ga.justreddy.wiki.reggwars.storage.type;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.replays.GameReplayCache;

import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface Storage {

    void createData();

    void createPlayer(IGamePlayer player);

    boolean doesPlayerExist(IGamePlayer player);

    void savePlayer(IGamePlayer player);

    void deletePlayer(IGamePlayer player);

    IGamePlayer loadPlayer(IGamePlayer player);

    IGamePlayer loadOfflinePlayer(String name);

    IGamePlayer loadOfflinePlayer(UUID uuid);

    void saveGameReplayCache(GameReplayCache cache);

    List<GameReplayCache> getGameReplayCaches(String name);

    GameReplayCache getGameReplayCache(String id);

    boolean doesBungeeFilesExist();

    boolean doesBungeeFileExist(String name);


    void createBungeeFiles(String name, String whatToCreate);

    void updateBungeeFiles(String name, String file);

    void loadBungeeFiles(String name);

    void saveBungeeFiles(String name, String toSave);

}
