package ga.justreddy.wiki.reggwars.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author JustReddy
 */
public class HikariStorage implements Storage {

    private final HikariConfig hikariConfig;
    private final HikariDataSource source;

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public HikariStorage(Config config, String host, String port, String username, String password, String database) {
        FileConfiguration fc = config.getConfig();
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.hikariConfig = new HikariConfig();
        this.hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.hikariConfig.setUsername(username);
        this.hikariConfig.setPassword(password);
        this.hikariConfig.addDataSourceProperty("cachePrepStmts", fc.getString("hikari.settings.cachePrepStmts"));
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSize", fc.getString("hikari.settings.prepStmtCacheSize"));
        this.hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", fc.getString("hikari.settings.prepStmtCacheSqlLimit"));
        this.hikariConfig.setPoolName(fc.getString("hikari.pool.name"));
        this.hikariConfig.setMaximumPoolSize(fc.getInt("hikari.pool.maximumPoolSize"));
        this.hikariConfig.setMinimumIdle(fc.getInt("hikari.pool.minimumIdle"));
        this.hikariConfig.setMaxLifetime(fc.getLong("hikari.pool.maxLifetime"));
        this.hikariConfig.setConnectionTimeout(fc.getLong("hikari.pool.connectionTimeout"));
        this.hikariConfig.setLeakDetectionThreshold(fc.getLong("hikari.pool.leakDetectionThreshold"));
        this.source = new HikariDataSource(this.hikariConfig);
    }

    private Connection getConnection() throws SQLException {
        return source.getConnection();
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
    public CompletableFuture<IGamePlayer> loadOfflinePlayer(String name) {
        return null;
    }

    @Override
    public CompletableFuture<IGamePlayer> loadOfflinePlayer(UUID uuid) {
        return null;
    }
}
