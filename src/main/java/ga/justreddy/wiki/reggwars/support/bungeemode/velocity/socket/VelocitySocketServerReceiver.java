package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.socket;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.Velocity;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class VelocitySocketServerReceiver {

    private final Velocity plugin;
    private final VelocitySocketServer socketServer;
    private final VelocitySocketServerSender sender;

    public VelocitySocketServerReceiver(VelocitySocketServer socketServer) {
        plugin = Velocity.getInstance();
        this.socketServer = socketServer;
        this.sender = socketServer.getSender();
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

    private void handlePacket(Packet packet) {
        switch (packet.getPacketType()) {
            case GAME_UPDATE: {
                GamePacket gamePacket = (GamePacket) packet;
                System.out.println("Game update: " + gamePacket.getGame().getName());
                plugin.getGames().put(gamePacket.getGame().getName(), gamePacket.getGame());
                sender.sendPacketToAll(gamePacket);
                break;
            }
            case GAME_REMOVE: {
                GamePacket gamePacket = (GamePacket) packet;
                plugin.getGames().remove(gamePacket.getGame().getName());
                sender.sendPacketToAllExcept(gamePacket, gamePacket.getGame().getServer());
                break;
            }
            case SERVER_GAMES_ADD: {
                ServerGamesPacket serverGamesPacket = (ServerGamesPacket) packet;
                for (BungeeGame game : serverGamesPacket.getGames()) {
                    plugin.getGames().put(game.getName(), game);
                }
                sender.sendPacketToAllExcept(serverGamesPacket, serverGamesPacket.getServer());
                break;
            }
            case GAME_JOIN: {
                JoinPacket joinPacket = (JoinPacket) packet;
                if (joinArena(joinPacket)) {
                    sender.sendPacketToServer(joinPacket, joinPacket.getGame().getServer());
                }
                break;
            }
            case GAME_LEAVE:
            case FORCE_START:
            case FORCE_STOP: {
                GamePlayerPacket playerPacket = (GamePlayerPacket) packet;
                sender.sendPacketToServer(playerPacket, playerPacket.getGame().getServer());
                break;
            }
            case KICK: {
                KickPacket kickPacket = (KickPacket) packet;
                sender.sendPacketToServer(kickPacket, kickPacket.getGame().getServer());
                break;
            }
            case BACK_TO_SERVER: {
                BackToServerPacket backToServerPacket = (BackToServerPacket) packet;
                sendBackToServer(backToServerPacket);
                break;
            }
            case GAMES_REQUEST: {
                GameRequestPacket gameRequestPacket = (GameRequestPacket) packet;
                sender.sendGamesPacket(gameRequestPacket.getServer(), new ArrayList<>(Bungee.getInstance().getGames().values()));
                break;
            }
            case CONFIG_UPDATE: {
                if (!(packet instanceof ConfigUpdatePacket)) return;
                ConfigUpdatePacket updatePacket = (ConfigUpdatePacket) packet;
                sender.sendPacketToAllExcept(updatePacket, updatePacket.getServer());
                break;
            }
        }
    }

    private boolean joinArena(JoinPacket joinPacket) {
        Optional<Player> optionalPlayer = plugin.getServer().getPlayer(joinPacket.getPlayer());
        if (!optionalPlayer.isPresent()) {
            return false;
        }

        Player player = optionalPlayer.get();

        Optional<ServerConnection> connection = player.getCurrentServer();
        if (!connection.isPresent()) {
            return false;
        }

        ServerConnection server = connection.get();

        if (joinPacket.isFirstJoin()) {
            plugin.getPlayerServers().put(joinPacket.getPlayer(), server.getServerInfo().getName());
        }
        if (!joinPacket.isLocalJoin()) {
            if (server.getServerInfo()
                    .getName().equals(joinPacket.getGame().getServer())) {
                return true;
            }
            Optional<RegisteredServer> optionalRegistered = plugin.getServer().getServer(joinPacket.getGame().getServer());
            if (!optionalRegistered.isPresent()) {
                player.sendMessage(Component.text("Fuck off"));
                return false;
            }
            RegisteredServer registeredServer = optionalRegistered.get();
            player.sendMessage(Component.text("CONNECTING TO " + registeredServer.getServerInfo().getName()));
            player.createConnectionRequest(registeredServer);
            return true;
        }

        return false;
    }

    private void sendBackToServer(BackToServerPacket backToServerPacket) {
        Optional<Player> optionalPlayer = plugin.getServer().getPlayer(backToServerPacket.getPlayer());
        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();

        String firstChoiceServer;
        String secondChoiceServer;

        if (backToServerPacket.getServerPriority().equals(BackToServerPacket.ServerPriority.PREVIOUS)) {
            firstChoiceServer = plugin.getPlayerServers().get(player.getUsername());
            secondChoiceServer = backToServerPacket.getLobby();
        } else {
            firstChoiceServer = backToServerPacket.getLobby();
            secondChoiceServer = plugin.getPlayerServers().get(player.getUsername());
        }

        if (!connectToServer(player, firstChoiceServer)) {
            connectToServer(player, secondChoiceServer);
        }

        plugin.getPlayerServers().remove(backToServerPacket.getPlayer());
    }

    private boolean connectToServer(Player player, String serverString) {
        if (serverString == null) {
            return false;
        }

        Optional<RegisteredServer> optionalRegisteredServer = plugin
                .getServer().getServer(serverString);
        if (!optionalRegisteredServer.isPresent()) {
            return false;
        }

        RegisteredServer registeredServer = optionalRegisteredServer.get();


        ServerInfo targetServer = registeredServer.getServerInfo();

        Optional<ServerConnection> connection = player.getCurrentServer();
        if (!connection.isPresent()) {
            return false;
        }

        ServerConnection server = connection.get();


        if (server.getServerInfo().equals(targetServer)) {
            return true;
        }

        if (socketServer.getSpigotSocket().containsKey(serverString)) {
            player.createConnectionRequest(registeredServer);
            return true;
        }


        return false;
    }


}
