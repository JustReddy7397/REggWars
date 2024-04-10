package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.socket;

import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.SocketConnection;
import ga.justreddy.wiki.reggwars.packets.socket.TargetPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.GamesPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.GamesSendPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.StringPacket;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerSender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SocketServerSenderMessenger implements IMessengerSender {

    private final SocketServerMessenger socketServer;
    private final LinkedBlockingQueue<TargetPacket> sendQueue = new LinkedBlockingQueue<>();

    public SocketServerSenderMessenger(SocketServerMessenger socketServer) {
        this.socketServer = socketServer;
    }

    public void send(ServerSocket socket) {
        while (!socket.isClosed()) {
            try {
                TargetPacket targetPacket = sendQueue.take();
                if (socket.isClosed()) return;
                Packet packet = targetPacket.getPacket();
                Set<String> servers = targetPacket.getServers();

                for (String server : servers) {
                    SocketConnection socketConnection = socketServer.getSpigotSocket().get(server);
                    if (socketConnection == null) continue;
                    ObjectOutputStream out = socketConnection.getOutputStream();

                    out.writeObject(packet);
                    out.flush();
                    out.reset();

                    if (socketServer.isDebug()) {
                        socketServer.log(Level.INFO, "[X] Sent packet: " + packet.getPacketType().toString() + " to server: " + server);
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        sendQueue.add(new TargetPacket(packet, new HashSet<>(socketServer.getSpigotSocket().keySet())));
    }

    @Override
    public void sendPacket(Packet packet, String server) {
        sendQueue.add(new TargetPacket(packet, Collections.singleton(server)));
    }

    @Override
    public void sendPacketToServer(Packet packet, String server) {
        sendPacket(packet, server);
    }

    @Override
    public void sendPacketToAllExcept(Packet packet, String exceptServer) {
        Set<String> servers = getServersExcept(exceptServer);
        sendQueue.add(new TargetPacket(packet, servers));
    }

    @Override
    public void sendPacketToAll(Packet packet) {
        sendQueue.add(new TargetPacket(packet, new HashSet<>(socketServer.getSpigotSocket().keySet())));
    }

    @Override
    public void sendAllGames(String server, List<BungeeGame> games) {
        GamesPacket gamesPacket = new GamesPacket(PacketType.GAMES_ADD, games);
        sendPacketToServer(gamesPacket, server);
    }

    @Override
    public void sendRemoveServerGames(String server) {
        StringPacket stringPacket = new StringPacket(PacketType.SERVER_GAMES_REMOVE, server);
        sendPacketToAllExcept(stringPacket, server);
    }

    @Override
    public Set<String> getServersExcept(String server) {
        Set<String> servers = new HashSet<>(socketServer.getSpigotSocket().keySet());
        servers.remove(server);
        return servers;
    }

    @Override
    public void sendGamesPacket(String server, List<BungeeGame> bungeeGames) {
        GamesSendPacket packet = new GamesSendPacket(server, bungeeGames);
        sendPacketToServer(packet, server);
    }
}
