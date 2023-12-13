package ga.justreddy.wiki.reggwars.bungee;

import ga.justreddy.wiki.reggwars.bungee.socket.SocketServer;
import ga.justreddy.wiki.reggwars.bungee.socket.SocketServerSender;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.storage.MYSQLStorage;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class Bungee extends Plugin {

    @Getter private static Bungee instance;
    @Getter private Storage storage;
    @Getter private BungeeConfig bungeeConfig;
    @Getter private final Map<String, BungeeGame> games = new HashMap<>();
    @Getter private final Map<String, String> playerServers = new HashMap<>();
    private SocketServer socketServer;


    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        storage = new MYSQLStorage(); // TODO
        this.bungeeConfig = new BungeeConfig("bungee-config.yml");
        this.socketServer = new SocketServer();
        this.socketServer.serverSetup(bungeeConfig.getConfig().getInt("socket-port"));

    }

    @Override
    public void onDisable() {
        socketServer.closeConnections();
        getProxy().getScheduler().cancel(this);
    }
}
