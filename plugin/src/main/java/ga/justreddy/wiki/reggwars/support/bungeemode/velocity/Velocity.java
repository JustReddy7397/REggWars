package ga.justreddy.wiki.reggwars.support.bungeemode.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.config.VelocityConfigManager;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.socket.VelocitySocketServer;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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

    @Getter private Logger logger;
    @Getter
    private static Velocity instance;
    @Getter
    private Path folder;
    @Getter
    private ProxyServer server;
    @Getter private final Map<String, BungeeGame> games = new HashMap<>();
    @Getter private final Map<String, String> playerServers = new HashMap<>();
    @Getter private VelocitySocketServer socketServer;
    @Getter private MiniMessage message;

    @Inject
    public Velocity(Logger logger, ProxyServer server, @DataDirectory Path folder) {
        instance = this;
        this.logger = logger;
        this.folder = folder;
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // TODO
        this.message = MiniMessage.miniMessage();
        VelocityConfigManager.initialize(folder.toFile());
        this.socketServer = new VelocitySocketServer();
        this.socketServer.serverSetup(VelocityConfigManager.getConfig().getInteger("socket-port"));
    }

    @Subscribe
    public void onProxyShutDown(ProxyShutdownEvent event) {
        if (socketServer != null) {
            socketServer.closeConnections();
            getServer().getScheduler().tasksByPlugin(this)
                    .forEach(ScheduledTask::cancel);

        }
    }


}
