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
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.rabbit.RabbitServerMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.redis.RedisServerMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.socket.SocketServerMessenger;
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
    @Getter private IMessenger<Velocity> messenger;
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
        this.message = MiniMessage.miniMessage();
        VelocityConfigManager.initialize(folder.toFile());
        switch (VelocityConfigManager.getConfig().getString("bungee.type").toLowerCase()) {
            case "socket":
                messenger = new SocketServerMessenger(VelocityConfigManager.getConfig().getInteger("bungee.socket.port"));
                break;
            case "rabbitmq":
                messenger = new RabbitServerMessenger(VelocityConfigManager.getConfig().getString("bungee.rabbitmq.host"),
                        VelocityConfigManager.getConfig().getInteger("bungee.rabbitmq.port"),
                        VelocityConfigManager.getConfig().getString("bungee.rabbitmq.username"),
                        VelocityConfigManager.getConfig().getString("bungee.rabbitmq.password"),
                        VelocityConfigManager.getConfig().getString("bungee.rabbitmq.vhost"));
                break;
            case "redis":
                messenger = new RedisServerMessenger(VelocityConfigManager.getConfig().getString("bungee.redis.host"),
                        VelocityConfigManager.getConfig().getInteger("bungee.redis.port"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + VelocityConfigManager.getConfig().getString("bungee.type").toLowerCase());
        }
        messenger.setup();
    }

    @Subscribe
    public void onProxyShutDown(ProxyShutdownEvent event) {
        if (message != null) {
            messenger.close();
            getServer().getScheduler().tasksByPlugin(this)
                    .forEach(ScheduledTask::cancel);

        }
    }

    public boolean isEmptyString(String path) {
        return  VelocityConfigManager.getConfig().getString(path) == null || VelocityConfigManager.getConfig().getString(path).isEmpty();
    }
}
