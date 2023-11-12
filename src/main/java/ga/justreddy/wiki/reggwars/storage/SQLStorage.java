package ga.justreddy.wiki.reggwars.storage;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.manager.ConfigManager;
import ga.justreddy.wiki.reggwars.model.replays.GameReplayCache;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Map;
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

    @Override
    public boolean doesBungeeFilesExist() {
        String qry = "SELECT * FROM eggwars_files";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(qry);
        ) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean doesBungeeFileExist(String name) {
        String qry = "SELECT * FROM eggwars_files WHERE name='" + name + "'";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(qry);
        ) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    @SneakyThrows
    @Override
    public void createBungeeFiles(String name, String whatToCreate) {
        if (doesBungeeFileExist(name)) return;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("INSERT INTO eggwars_files " +
                    "(name, file) VALUES ('" + name + "', '" + whatToCreate + "')");
        }
    }

    @Override
    @SneakyThrows
    public void updateBungeeFiles(String name, String file) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            for (Map.Entry<String, Config> entry : ConfigManager.getManager().getConfigs().entrySet()) {
                if (entry.getKey().equals("config")) continue;
                Config config = entry.getValue();
                config.reload();
                String toSave = ConfigManager.getManager().parseConfiguration(config.getConfig());
                boolean exists = doesBungeeFileExist(entry.getKey());
                if (exists) {
                    saveBungeeFiles(entry.getKey(), toSave);
                } else {
                    createBungeeFiles(entry.getKey(), toSave);
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public void loadBungeeFiles(String name) {
        String qry = "SELECT * FROM eggwars_files WHERE name='" + name + "'";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(qry);
        ) {
            if (!rs.next()) return;
            String contents = rs.getString("file");
            Config config = ConfigManager.getManager().getConfigs().getOrDefault(rs.getString("name"), null);
            if (config == null) return;
            File file = config.getFile();
            FileConfiguration configuration = config.getConfig();
            ConfigManager.getManager().setConfigValues(contents, file);
            configuration.save(file);
            config.reload();
        }
    }

    @SneakyThrows
    @Override
    public void saveBungeeFiles(String name, String file) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("UPDATE eggwars_files SET file='" + file + "' WHERE name='" + name + "'");
        }
    }
}
