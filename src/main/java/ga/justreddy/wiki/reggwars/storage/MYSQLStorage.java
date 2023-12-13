package ga.justreddy.wiki.reggwars.storage;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.storage.type.Storage;

import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class MYSQLStorage implements Storage {
    @Override
    public void createData() {

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

}
