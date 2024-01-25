package ga.justreddy.wiki.reggwars.storage;

import com.avaje.ebean.FutureIds;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerCosmetics;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerQuests;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerQuickBuy;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerSettings;
import ga.justreddy.wiki.reggwars.model.entity.data.adapters.PlayerSettingsAdapter;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import ga.justreddy.wiki.reggwars.utils.FutureUtil;
import ga.justreddy.wiki.reggwars.utils.JsonUtil;
import org.bson.Document;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        if (doesPlayerExist(player)) return;
        Document document = new Document("_id", player.getUniqueId());
        document.append("name", player.getName());
        String playerJson = JsonUtil.toJson(player.getSettings(), PlayerSettingsAdapter.class, PlayerSettings.class);
        document.append("settings", Document.parse(playerJson));
        String cosmeticJson = JsonUtil.toJson(player.getCosmetics(), PlayerCosmetics.class);
        document.append("cosmetics", Document.parse(cosmeticJson));
        String quickBuyJson = JsonUtil.toJson(player.getQuickBuy(), PlayerQuickBuy.class);
        document.append("quickBuy", Document.parse(quickBuyJson));
        String questsJson = JsonUtil.toJson(player.getQuests(), PlayerSettings.class);
        document.append("quests", Document.parse(questsJson));
        getCollection("players").insertOne(document);
    }

    @Override
    public boolean doesPlayerExist(IGamePlayer player) {
        return getCollection("players").find(new Document("_id", player.getUniqueId())).first() != null;
    }

    @Override
    public void savePlayer(IGamePlayer player) {

    }

    @Override
    public void deletePlayer(IGamePlayer player) {

    }

    @Override
    public IGamePlayer loadPlayer(IGamePlayer player) {
        if (!doesPlayerExist(player)) {
            createPlayer(player);
            return player;
        }

        Document foundDocument = getCollection("players").find(new Document("_id", player.getUniqueId())).first();
        if (foundDocument == null) return null;
        Document settings = (Document) foundDocument.get("settings");
        Document cosmetics = (Document) foundDocument.get("cosmetics");
        Document quickBuy = (Document) foundDocument.get("quickBuy");
        Document quests = (Document) foundDocument.get("quests");
        if (settings == null || cosmetics == null || quickBuy == null || quests == null) return null;
        PlayerSettings playerSettings = JsonUtil.fromJson(settings.toJson(), PlayerSettings.class);
        PlayerCosmetics playerCosmetics = JsonUtil.fromJson(cosmetics.toJson(), PlayerCosmetics.class);
        PlayerQuickBuy playerQuickBuy = JsonUtil.fromJson(quickBuy.toJson(), PlayerQuickBuy.class);
        PlayerQuests playerQuests = JsonUtil.fromJson(quests.toJson(), PlayerQuests.class);
        player.setSettings(playerSettings);
        player.setCosmetics(playerCosmetics);
        player.setQuickBuy(playerQuickBuy);
        player.setQuests(playerQuests);
        return player;
    }

    @Override
    public CompletableFuture<IGamePlayer> loadOfflinePlayer(String name) {
        return FutureUtil.futureAsync(() -> {
            Document document = getCollection("players").find(new Document("name", name)).first();
            if (document == null) return null;
            UUID uuid = (UUID) document.get("_id");
            String playerName = (String) document.get("name");
            IGamePlayer player = new GamePlayer(uuid, playerName);
            return loadPlayer(null);
        });
    }

    @Override
    public CompletableFuture<IGamePlayer> loadOfflinePlayer(UUID uuid) {
        return FutureUtil.futureAsync(() -> {
            Document document = getCollection("players").find(new Document("_id", uuid)).first();
            if (document == null) return null;
            String playerName = (String) document.get("name");
            IGamePlayer player = new GamePlayer(uuid, playerName);
            return loadPlayer(null);
        });
    }
}
