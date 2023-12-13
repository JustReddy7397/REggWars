package ga.justreddy.wiki.reggwars.socket;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.bungee.Core;
import ga.justreddy.wiki.reggwars.bungee.ServerMode;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author JustReddy
 */
@Getter
public class SocketClient {

    private final REggWars plugin;
    private final Logger logger;
    private final boolean debug;
    private final String host;
    private final int port;
    private final String server;
    private final SocketClientSender sender;
    private final SocketClientReceiver receiver;
    private Socket socket;


    public SocketClient(String host, int port, String server) {
        plugin = REggWars.getInstance();
        logger = Bukkit.getLogger();
        debug = plugin.getConfig().getBoolean("debug");

        sender = new SocketClientSender(this);
        receiver = new SocketClientReceiver(this);

        this.host = host;
        this.port = port;
        this.server = server;
    }

    public void clientSetup() {
        new BukkitRunnable() {
            boolean msg = socket != null;

            @Override
            public void run() {
                if (clientConnect(host, port, server)) {
                    this.cancel();
                    return;
                }

                if (!msg) {
                    msg = true;
                    log(Level.WARNING, "[!] Couldn't connect to the bungeecord server. Plugin will try to connect until it succeeds.");
                }
            }

        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    private boolean clientConnect(String host, int port, String server) {
        try {
            socket = new Socket(host, port);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeUTF(server);
            out.flush();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> sender.send(socket, out));
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> clientReceive(in));

            if (Core.MODE == ServerMode.BUNGEE) {
                List<BungeeGame> games = new ArrayList<>();
                for (IGame game : new ArrayList<>(GameManager.getManager().getGames().values())) {
                    if (isDebug()) {
                        log(Level.INFO, "[X] Adding game: " + game.getName());
                    }
                    BungeeGame bungeeGame = new BungeeGame(
                            game.getName(),
                            plugin.getServerName(),
                            game.getMaxPlayers(),
                            game.getGameState(),
                            new ArrayList<>());
                    games.add(bungeeGame);

                    GameManager.getManager().getBungeeGames()
                            .put(game.getName(), bungeeGame);
                }
                sender.sendGamesPacket(plugin.getServerName(), games);
            } else if (Core.MODE == ServerMode.LOBBY) {
                sender.sendRequestPacket(plugin.getServerName());
            }

            log(Level.INFO, "§aSuccessfully connected to the bungeecord server!");
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    private void clientReceive(ObjectInputStream in) {
        try {
            receiver.clientMessageReader(socket, in);
        } catch (IOException e) {
            if (!socket.isClosed()) {
                closeConnections();

                clientSetup();
                log(Level.WARNING, "[!] Lost connection with the bungeecord server. Trying to reconnect...");

                if (debug) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnections() {
        try {
            getSender().close();
            socket.close();
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    public void log(Level level, String message, String... args) {
        logger.log(level, MessageFormat.format("[{0}] {1}", plugin.getName(), message), args);
    }


}
