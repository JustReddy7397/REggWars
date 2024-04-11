package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.socket;

import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.socket.SocketClientReceiver;
import ga.justreddy.wiki.reggwars.socket.SocketClientSender;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerSender;
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
public class SocketClientMessenger implements IMessenger<REggWars> {

    private final REggWars plugin;
    private final Logger logger;
    private final boolean debug;
    private final String host;
    private final int port;
    private final String server;
    private final IMessengerSender sender;
    private final IMessengerReceiver receiver;
    private Socket socket;


    public SocketClientMessenger(String host, int port, String server) {
        plugin = REggWars.getInstance();
        logger = Bukkit.getLogger();
        debug = plugin.getConfig().getBoolean("debug");

/*
        sender = new SocketClientSender(this);
        receiver = new SocketClientReceiver(this);
*/

        this.host = host;
        this.port = port;
        this.server = server;
    }

    @Override
    public void setup() {
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

    @Override
    public void connect() {

    }

    @Override
    public void close() {

    }

    @Override
    public void receive() {

    }


    private boolean clientConnect(String host, int port, String server) {
        try {
            socket = new Socket(host, port);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeUTF(server);
            out.flush();

/*
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> sender.send(socket, out));
*/
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> clientReceive(in));

            if (Core.MODE == ServerMode.BUNGEE) {
                List<BungeeGame> games = new ArrayList<>();
                for (IGame game : new ArrayList<>(GameManager.getManager().getGames().values())) {
                    if (debug) {
                        log(Level.INFO, "[X] Adding game: " + game.getName());
                    }
                    BungeeGame bungeeGame = new BungeeGame(
                            game.getName(),
                            plugin.getServerName(),
                            game.getMaxPlayers(),
                            game.getGameState(),
                            game.getGameMode(),
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
/*
            receiver.clientMessageReader(socket, in);
*/
        } catch (IOException e) {
            if (!socket.isClosed()) {
                close();

                setup();
                log(Level.WARNING, "[!] Lost connection with the bungeecord server. Trying to reconnect...");

                if (debug) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public REggWars getPlugin() {
        return plugin;
    }

    @Override
    public IMessengerSender getSender() {
        return sender;
    }

    @Override
    public IMessengerReceiver getReceiver() {
        return receiver;
    }

    public void log(Level level, String message, String... args) {
        logger.log(level, MessageFormat.format("[{0}] {1}", plugin.getName(), message), args);
    }


}
