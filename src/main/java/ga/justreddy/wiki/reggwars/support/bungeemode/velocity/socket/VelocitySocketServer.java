package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.socket;

import ga.justreddy.wiki.reggwars.packets.socket.SocketConnection;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.Velocity;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.config.VelocityConfigManager;
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
public class VelocitySocketServer {

    private final Velocity velocity;
    private final Logger logger;
    private final boolean debug;
    private final Map<String, SocketConnection> spigotSocket = new HashMap<>();
    private final VelocitySocketServerSender sender;
    private final VelocitySocketServerReceiver receiver;
    private ServerSocket serverSocket;

    public VelocitySocketServer() {
        velocity = Velocity.getInstance();
        logger = Velocity.getInstance().getLogger();
        debug = VelocityConfigManager.getConfig().getBoolean("debug");
        sender = new VelocitySocketServerSender(this);
        receiver = new VelocitySocketServerReceiver(this);
    }

    public void serverSetup(int port) {
        velocity.getServer().getScheduler()
                .buildTask(velocity, () -> {
                    try {
                        serverSocket = new ServerSocket(port);

                        velocity.getServer().getScheduler()
                                .buildTask(velocity, () -> sender.send(serverSocket))
                                .schedule();



                        log(Level.INFO, "[X] Successfully started the socket server!");

                        acceptConnection(serverSocket);
                    } catch (IOException e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                })
                .schedule();
    }

    private void acceptConnection(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            if (debug) {
                log(Level.INFO, "[X] Accepted connection from client {0}", socket.getInetAddress().toString());
            }

            velocity.getServer().getScheduler()
                            .buildTask(velocity, () -> acceptConnection(serverSocket))
                    .schedule();


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

        sender.sendAllGames(server, new ArrayList<>(velocity.getGames().values()));

        log(Level.INFO, "[X] Successfully connected to the {0} server!", server);

        serverReceive(server, objectInputStream);
    }

    private void serverReceive(String server, ObjectInputStream in) {
        try {
            receiver.serverMessageReader(serverSocket, server, in);
        } catch (IOException e) {
            log(Level.WARNING, "[!] Disconnected from the {0} server!", server);

            for (String game : new ArrayList<>(velocity.getGames().keySet())) {
                if (velocity.getGames().get(game).getServer().equals(server)) {
                    velocity.getGames().remove(game);
                }
            }

            sender.sendRemoveServerGames(server);
            spigotSocket.remove(server);

            if (debug) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnections() {
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

    public void log(Level level, String message, String... args) {
        logger.log(level, MessageFormat.format("[{0}] {1}", "REggWars", message), args);
    }


}
