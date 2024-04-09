package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.socket;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerSender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SocketServerReceiverMessenger implements IMessengerReceiver {
    private final Bungee plugin;
    private final SocketServerMessenger socketServer;
    private final IMessengerSender sender;

    public SocketServerReceiverMessenger(IMessenger<Bungee> socketServer) {
        this.socketServer = (SocketServerMessenger) socketServer;
        this.sender = socketServer.getSender();
        this.plugin = socketServer.getPlugin();
    }

    public void serverMessageReader(ServerSocket socket, String server, ObjectInputStream stream) throws IOException {
        while (!socket.isClosed()) {
            try {
                Object object = stream.readObject();
                Packet packet = (Packet) object;

                if (socketServer.isDebug()) {
                    socketServer.log(Level.INFO, "[X] Packet received: " + packet.getPacketType().toString() + " from server: " + server);
                }
                handlePacket(packet);

            } catch (ClassNotFoundException | IOException e) {
                if (socketServer.isDebug()) e.fillInStackTrace();
            }
        }
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}
