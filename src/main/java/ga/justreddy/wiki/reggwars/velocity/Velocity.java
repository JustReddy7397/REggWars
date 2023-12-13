package ga.justreddy.wiki.reggwars.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ga.justreddy.wiki.reggwars.bungee.BungeeConfig;
import ga.justreddy.wiki.reggwars.bungee.socket.SocketServer;
import ga.justreddy.wiki.reggwars.manager.ConfigManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.velocity.config.VelocityConfig;
import ga.justreddy.wiki.reggwars.velocity.config.VelocityConfigManager;
import ga.justreddy.wiki.reggwars.velocity.socket.VelocitySocketServer;
import lombok.Getter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
@Plugin(
        id = "reggwars",
        name = "ReggWars",
        version = "1.0",
        description = "Velocity support for per-server-games",
        authors = {"JustReddy"}
)
public class Velocity {

    @Getter
    private static Velocity instance;
    @Getter
    private Path folder;
    @Getter
    private ProxyServer server;
    @Getter private final Map<String, BungeeGame> games = new HashMap<>();
    @Getter private final Map<String, String> playerServers = new HashMap<>();
    @Getter private VelocitySocketServer socketServer;

    @Inject
    public Velocity(ProxyServer server, Path folder) {
        instance = this;
        this.folder = folder;
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // TODO
        VelocityConfigManager.initialize(folder.toFile());
        this.socketServer = new VelocitySocketServer();
        this.socketServer.serverSetup(VelocityConfigManager.getConfig().getInteger("socket-port"));
    }


}
