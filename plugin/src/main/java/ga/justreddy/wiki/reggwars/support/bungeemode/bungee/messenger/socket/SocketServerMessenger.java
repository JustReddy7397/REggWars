package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.socket;

import ga.justreddy.wiki.reggwars.packets.socket.SocketConnection;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerSender;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

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
public class SocketServerMessenger implements IMessenger<Bungee> {

    private final Bungee bungee;
    private final Logger logger;
    private final boolean debug;
    private final Map<String, SocketConnection> spigotSocket = new HashMap<>();
    private final IMessengerSender sender;
    private final IMessengerReceiver receiver;
    private ServerSocket serverSocket;

    private final int port;

    public SocketServerMessenger(int port) {
        this.port = port;
        bungee = Bungee.getInstance();
        logger = ProxyServer.getInstance().getLogger();
        debug = bungee.getBungeeConfig().getConfig().getBoolean("debug");
        this.sender = new SocketServerSenderMessenger(this);
        this.receiver = new SocketServerReceiverMessenger(this);
    }

    @Override
    public void setup() {
        ProxyServer.getInstance().getScheduler().runAsync(bungee, () -> {
            try {
                serverSocket = new ServerSocket(port);

                ProxyServer.getInstance().getScheduler().runAsync(bungee, () -> ((SocketServerSenderMessenger)sender).send(serverSocket));

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
    public Bungee getPlugin() {
        return null;
    }

    @Override
    public IMessengerSender getSender() {
        return null;
    }

    @Override
    public IMessengerReceiver getReceiver() {
        return null;
    }

    public void log(Level level, String message, String... args) {
        logger.log(level, MessageFormat.format("[{0}] {1}", bungee.getDescription().getName(), message), args);
    }

    private void acceptConnection(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            if (debug) {
                log(Level.INFO, "[X] Accepted connection from client {0}", socket.getInetAddress().toString());
            }

            ProxyServer.getInstance().getScheduler().runAsync(bungee, () -> acceptConnection(serverSocket));

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
