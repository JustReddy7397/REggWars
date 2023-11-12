package ga.justreddy.wiki.reggwars.bungee.server;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.bungee.Bungee;
import ga.justreddy.wiki.reggwars.bungee.config.BungeeConfig;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.BaseBalancer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.server.BungeeServer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.server.EggWarsServer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.server.LobbyServer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.type.LeastConnection;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.type.MostConnection;
import ga.justreddy.wiki.reggwars.bungee.server.listener.ServerListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JustReddy
 */
public class ServerManager {

    private final Map<String, BungeeServer> activeServers;
    private final Map<ServerType, BaseBalancer<BungeeServer>> balancers;
    private final Map<ServerInfo, ServerPing> cache = new HashMap<>();

    private ServerManager() {
        this.activeServers = new HashMap<>();
        this.balancers = new HashMap<>();
        for (ServerType type : ServerType.values()) {
            this.balancers.put(type, type == ServerType.LOBBY ? new LeastConnection<>() : new MostConnection<>());
        }
    }

    public void start() {
        ProxyServer.getInstance().registerChannel("EggWarsAPI");
        ProxyServer.getInstance().getPluginManager().registerListener(Bungee.getInstance(), new ServerListener());
        BungeeConfig config = BungeeConfig.getConfig("servers");
        List<String> fetchList = new ArrayList<>();
        fetchList.addAll(config.getStringList("lobbies"));
        fetchList.addAll(config.getStringList("arenas"));
        ProxyServer.getInstance().getScheduler().schedule(Bungee.getInstance(), () -> {
            for (String toFetch : fetchList) {
                ServerInfo info = ProxyServer.getInstance().getServerInfo(toFetch);
                if (info == null) continue;
                ServerPing ping = cache.get(info);
                if (ping == null) {
                    if (!(info instanceof InetSocketAddress)) continue;
                    ping = new ServerPing((InetSocketAddress) info.getSocketAddress());
                    cache.put(info, ping);
                }
                try {
                    ping.fetch();
                } catch (Exception ex) {
                    // Down?
                }

                updateActiveServer(info, ping);

            }
        }, 0, 2, TimeUnit.SECONDS);
    }


    public void updateActiveServer(ServerInfo info, ServerPing ping) {
        BungeeServer server = activeServers.get(info.getName());
        if (ping.getMotd() == null || !ping.getMotd().contains(";")) {
            if (server != null) {
                server.setJoinEnabled(false);
            }
            return;
        }
        String motd = ChatColor.stripColor(ping.getMotd());

        String[] splitted = motd.split(" ; ");
        ServerType type;
        String map = null;
        GameState state = null;

        if (splitted.length < 2) {
            type = ServerType.LOBBY;
        } else {
            type = ServerType.valueOf(splitted[0]);
            map = splitted[1];
            state = GameState.valueOf(splitted[2]);
        }

        if (server == null) {
            if (type == ServerType.LOBBY) {
                server = new LobbyServer(info.getName(), ping.getMax());
            } else {
                server = new EggWarsServer(info.getName(), ping.getMax(), state);
            }

            activeServers.put(info.getName(), server);
            balancers.get(type).add(info.getName(), server);
        }

        if (server instanceof EggWarsServer) {
            EggWarsServer es = (EggWarsServer) server;
            es.setMap(map);
            es.setState(state);
            es.setJoinEnabled(!es.isInProgress() && !server.isFull());
        } else {
            server.setJoinEnabled(!server.isFull());
        }



    }

    public BaseBalancer<BungeeServer> getBalancer(ServerType type) {
        return this.balancers.get(type);
    }

    private static final ServerManager INSTANCE = new ServerManager();

    public static ServerManager getManager() {
        return INSTANCE;
    }


}
