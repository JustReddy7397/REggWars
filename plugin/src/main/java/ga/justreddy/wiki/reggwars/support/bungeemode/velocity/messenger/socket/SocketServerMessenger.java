package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.socket;

import ga.justreddy.wiki.reggwars.packets.socket.SocketConnection;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.config.VelocityConfigManager;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerSender;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.Velocity;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author JustReddy
 */
@Getter
public class SocketServerMessenger implements IMessenger<Velocity> {

    private final Velocity bungee;
    private final Logger logger;
    private final boolean debug;
    private final Map<String, SocketConnection> spigotSocket = new HashMap<>();
    private final IMessengerSender sender;
    private final IMessengerReceiver receiver;
    private ServerSocket serverSocket;

    private final int port;

    public SocketServerMessenger(int port) {
        this.port = port;
        bungee = Velocity.getInstance();
        logger = bungee.getLogger();
        debug = VelocityConfigManager.getConfig().getBoolean("debug");
        this.sender = new SocketServerSenderMessenger(this);
        this.receiver = new SocketServerReceiverMessenger(this);
    }

    @Override
    public void setup() {
        bungee.getServer().getScheduler().buildTask(bungee, () -> {
            try {
                serverSocket = new ServerSocket(port);

                bungee.getServer().getScheduler()
                        .buildTask(bungee, () -> acceptConnection(serverSocket));

                log(Level.INFO, "[X] Successfully started the socket server!");

                acceptConnection(serverSocket);
            } catch (IOException e) {
                if (debug) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void connect() {

    }

    @Override
    public void close() {
        try {
            serverSocket.close();
            for (SocketConnection socketConnection : spigotSocket.values()) {
                socketConnection.getSocket().close();
            }
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receive() {

    }

    @Override
    public Velocity getPlugin() {
        return Velocity.getInstance();
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
        logger.log(level, MessageFormat.format("[{0}] {1}", "REggWars", message), args);
    }

    private void acceptConnection(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            if (debug) {
                log(Level.INFO, "[X] Accepted connection from client {0}", socket.getInetAddress().toString());
            }

            bungee.getServer().getScheduler()
                    .buildTask(bungee, () -> acceptConnection(serverSocket));

            clientSetup(socket);
        } catch (IOException e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    private void clientSetup(Socket socket) throws IOException {
        if (socket.isClosed()) return;
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        String server = objectInputStream.readUTF();

        SocketConnection socketConnection = new SocketConnection(socket, objectOutputStream);
        spigotSocket.put(server, socketConnection);

        sender.sendAllGames(server, new ArrayList<>(bungee.getGames().values()));

        log(Level.INFO, "[X] Successfully connected to the {0} server!", server);

        serverReceive(server, objectInputStream);
    }

    private void serverReceive(String server, ObjectInputStream in) {
        try {
            ((SocketServerReceiverMessenger)receiver).serverMessageReader(serverSocket, server, in);
        } catch (IOException e) {
            log(Level.WARNING, "[!] Disconnected from the {0} server!", server);

            for (String game : new ArrayList<>(bungee.getGames().keySet())) {
                if (bungee.getGames().get(game).getServer().equals(server)) {
                    bungee.getGames().remove(game);
                }
            }

            sender.sendRemoveServerGames(server);
            spigotSocket.remove(server);

            if (debug) {
                e.printStackTrace();
            }
        }
    }

}
