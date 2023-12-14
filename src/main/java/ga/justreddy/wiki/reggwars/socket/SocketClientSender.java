package ga.justreddy.wiki.reggwars.socket;

import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SocketClientSender {

    private final SocketClient socketClient;
    private final LinkedBlockingQueue<Packet> sendQueue = new LinkedBlockingQueue<>();

    public SocketClientSender(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    // Run this asynchronously!
    public void send(Socket socket, ObjectOutputStream out) {
        while (!socket.isClosed()) {
            try {
                Packet packet = sendQueue.take();
                if (packet.getPacketType() == null) return;

                out.writeObject(packet);
                out.flush();
                out.reset();

                if (socketClient.isDebug()) {
                    socketClient.log(Level.INFO, "[X] Sent packet: " + packet.getPacketType().toString());
                }
            } catch (IOException | InterruptedException e) {
                e.fillInStackTrace();
            }
        }
    }

    public void close() {
        sendQueue.add(new Packet(null));
    }

    private void sendPacket(Packet packet) {
        sendQueue.add(packet);
    }

    public void sendJoinPacket(BungeeGame game, String player, boolean joinServer, boolean localJoin) {
        JoinPacket joinPacket = new JoinPacket(game, player, joinServer, localJoin);
        sendPacket(joinPacket);
    }

    public void sendGamesPacket(String server, List<BungeeGame> games) {
        ServerGamesPacket serverGamesPacket = new ServerGamesPacket(PacketType.SERVER_GAMES_ADD, server, games);
        sendPacket(serverGamesPacket);
    }

    public void sendRequestPacket(String server) {
        GameRequestPacket requestPacket = new GameRequestPacket(server);
        sendPacket(requestPacket);
    }

    public void sendUpdateGamePacket(BungeeGame games) {
        GamePacket gamePacket = new GamePacket(PacketType.GAME_UPDATE, games);
        sendPacket(gamePacket);
    }

    public void sendRemoveGamePacket(BungeeGame game) {
        GamePacket gamePacket = new GamePacket(PacketType.GAME_REMOVE, game);
        sendPacket(gamePacket);
    }

    public void sendBackToServerPacket(BackToServerPacket.ServerPriority serverPriority, String player, String lobby) {
        BackToServerPacket backToServerPacket = new BackToServerPacket(serverPriority, player, lobby);
        sendPacket(backToServerPacket);
    }

    public void sendForceStartPacket(String player, BungeeGame game) {
        GamePlayerPacket gamePlayerPacket = new GamePlayerPacket(PacketType.FORCE_START, game, player);
        sendPacket(gamePlayerPacket);
    }

    public void sendForceStopPacket(String player, BungeeGame game) {
        GamePlayerPacket arenaPlayerPacket = new GamePlayerPacket(PacketType.FORCE_STOP, game, player);
        sendPacket(arenaPlayerPacket);
    }

    public void sendKickPacket(String player, String target, BungeeGame game) {
        KickPacket kickPacket = new KickPacket(player, target, game);
        sendPacket(kickPacket);
    }

    public void sendConfigUpdatePacket(String server, Config config) {
        ConfigUpdatePacket configUpdatePacket = new ConfigUpdatePacket(server, new SerializableConfig(config));
        sendPacket(configUpdatePacket);
    }

}
