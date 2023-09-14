package ga.justreddy.wiki.reggwars.storage;

import com.avaje.ebeaninternal.server.query.LimitOffsetList;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.replays.GameReplayCache;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class SQLStorage implements Storage {

    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection("jdbc:sqlite:" + REggWars.getInstance()
                .getDataFolder() + "/eggwars.db");
    }

    @Override
    public void createPlayer(IGamePlayer player) {

    }

    @Override
    public boolean doesPlayerExist(IGamePlayer player) {
        return false;
    }

    @Override
    public void savePlayer(IGamePlayer player) {

    }

    @Override
    public void deletePlayer(IGamePlayer player) {

    }

    @Override
    public IGamePlayer loadPlayer(IGamePlayer player) {
        return null;
    }

    @Override
    public IGamePlayer loadOfflinePlayer(String name) {
        return null;
    }

    @Override
    public IGamePlayer loadOfflinePlayer(UUID uuid) {
        return null;
    }

    @Override
    public void saveGameReplayCache(GameReplayCache cache) {

    }

    @Override
    public List<GameReplayCache> getGameReplayCaches(String name) {
        return null;
    }

    @Override
    public GameReplayCache getGameReplayCache(String id) {
        return null;
    }
}