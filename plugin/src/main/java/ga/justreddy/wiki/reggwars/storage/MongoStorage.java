package ga.justreddy.wiki.reggwars.storage;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class MongoStorage implements Storage {

    private final String uri;

    public MongoStorage(String uri) {
        this.uri = uri;
    }

    public MongoCollection<Document> getCollection(String name) {
        try (MongoClient client = new MongoClient(uri)) {
            return client.getDatabase("reggwars").getCollection(name);
        }
    }

    @Override
    public void createData() {
        // EMPTY LOL
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
