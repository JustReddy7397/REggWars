package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.socket;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.classes.BackToServerPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.JoinPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.MessagePacket;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.Velocity;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.config.VelocityConfigManager;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerSender;
import ga.justreddy.wiki.reggwars.utils.iridium.IridiumColorAPI;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SocketServerReceiverMessenger implements IMessengerReceiver {
    private final Velocity plugin;
    private final SocketServerMessenger socketServer;
    private final IMessengerSender sender;

    public SocketServerReceiverMessenger(IMessenger<Velocity> socketServer) {
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
        if (packet.getServer().equalsIgnoreCase("REggWarsBungee")) {
            return;
        }

        switch (packet.getPacketType()) {
            case MESSAGE: {
                if (!(packet instanceof MessagePacket)) return;
                MessagePacket messagePacket = (MessagePacket) packet;
                Player proxiedPlayer = Velocity.getInstance().getServer().getPlayer(messagePacket.getUuid()).orElse(null);
                if (proxiedPlayer == null) {
                    return;
                }
                for (String message : messagePacket.getMessages()) {
                    proxiedPlayer.sendMessage(Component.text(IridiumColorAPI.process(message)));
                }

                break;
            }
            case BACK_TO_SERVER: {
                if (!(packet instanceof BackToServerPacket)) {
                    return;
                }
                sendBackToServer((BackToServerPacket) packet);
                break;
            }
            case GAME_JOIN: {
                JoinPacket joinPacket = (JoinPacket) packet;
                if (joinArena(joinPacket)) {
                    packet.setServer("REggWarsBungee");
                    sender.sendPacket(packet);
                    break;
                }
            }
        }

    }

    private boolean joinArena(JoinPacket joinPacket) {
        Player proxiedPlayer = Velocity.getInstance().getServer().getPlayer(joinPacket.getPlayer()).orElse(null);
        if (proxiedPlayer == null) {
            return false;
        }


        if (joinPacket.isFirstJoin()) {
            Velocity.getInstance().getPlayerServers().put(joinPacket.getPlayer(), proxiedPlayer.getCurrentServer().get().getServerInfo().getName());
        }
        if (!joinPacket.isLocalJoin()) {
            if (proxiedPlayer.getCurrentServer().get().getServerInfo()
                    .getName().equals(joinPacket.getGame().getServer())) {
                return true;
            }
            ServerInfo info = Velocity.getInstance().getServer().getServer(joinPacket.getGame().getServer()).get().getServerInfo();
            if (info == null) {
                if (!Velocity.getInstance().isEmptyString("messages.no-game-found")) {
                    proxiedPlayer.sendMessage(Component.text(IridiumColorAPI.process(VelocityConfigManager.getConfig().getString("messages.no-game-found").replaceAll("<type>", joinPacket.getGame().getGameMode().getName()))));
                }
                return false;
            }
            if (proxiedPlayer.getCurrentServer().get().getServerInfo().getName().equalsIgnoreCase(info.getName())) return true;
            if (!Velocity.getInstance().isEmptyString("messages.connecting")) {
                proxiedPlayer.sendMessage(Component.text(IridiumColorAPI.process(VelocityConfigManager.getConfig()
                        .getString("messages.connecting").replaceAll("<type>", joinPacket.getGame().getGameMode().getName()))));
            }
            RegisteredServer server = Velocity.getInstance().getServer().getServer(info.getName()).orElse(null);
            if (server != null) {
                proxiedPlayer.createConnectionRequest(server).fireAndForget();
            }
            return true;
        }

        return false;
    }

    private void sendBackToServer(BackToServerPacket backToServerPacket) {
        Player proxiedPlayer = Velocity.getInstance().getServer().getPlayer(backToServerPacket.getPlayer()).orElse(null);
        if (proxiedPlayer == null) {
            return;
        }

        String firstChoiceServer;
        String secondChoiceServer;

        if (backToServerPacket.getServerPriority().equals(BackToServerPacket.ServerPriority.PREVIOUS)) {
            firstChoiceServer = Velocity.getInstance().getPlayerServers().get(proxiedPlayer.getUsername());
            secondChoiceServer = backToServerPacket.getLobby();
        } else {
            firstChoiceServer = backToServerPacket.getLobby();
            secondChoiceServer = Velocity.getInstance().getPlayerServers().get(proxiedPlayer.getUsername());
        }

        if (!connectToServer(proxiedPlayer, firstChoiceServer)) {
            connectToServer(proxiedPlayer, secondChoiceServer);
        }

        Velocity.getInstance().getPlayerServers().remove(backToServerPacket.getPlayer());
    }

    private boolean connectToServer(Player player, String serverString) {
        if (serverString == null) {
            return false;
        }

        RegisteredServer targetServer = Velocity.getInstance().getServer().getServer(serverString).orElse(null);

        if (targetServer == null) {
            return false;
        }

        ServerConnection currentServer = player.getCurrentServer().orElse(null);

        if (currentServer == null) {
            return false;
        }

        if (currentServer.getServerInfo().getName().equalsIgnoreCase(targetServer.getServerInfo().getName())) {
            return true;
        }

        player.createConnectionRequest(targetServer)
                .fireAndForget();

        return true;
    }

}
