package ga.justreddy.wiki.reggwars.support.bungeemode.bungee;

import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.socket.SocketServerMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.socket.SocketServer;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessenger;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class Bungee extends Plugin {

    @Getter
    private static Bungee instance;
    @Getter
    private BungeeConfig bungeeConfig;
    @Getter
    private final Map<String, BungeeGame> games = new HashMap<>();
    @Getter
    private final Map<String, String> playerServers = new HashMap<>();
    @Getter
    private IMessenger<Bungee> messenger;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        this.bungeeConfig = new BungeeConfig("bungee-config.yml");
        switch (bungeeConfig.getConfig().getString("bungee.type").toLowerCase()) {
            case "socket":
                messenger = new SocketServerMessenger(bungeeConfig.getConfig().getInt("bungee.socket.port"));
                break;
            case "rabbitmq":
                break;
            case "redis":
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + bungeeConfig.getConfig().getString("bungee.type").toLowerCase());
        }

    }

    @Override
    public void onDisable() {
        messenger.close();
        getProxy().getScheduler().cancel(this);
    }
}
