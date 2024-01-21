package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.socket;

import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;


/**
 * @author JustReddy
 */
public class SocketServerReceiver {

    private final Bungee plugin;
    private final SocketServer socketServer;
    private final SocketServerSender sender;

    public SocketServerReceiver(SocketServer socketServer) {
        plugin = Bungee.getInstance();
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
            case MESSAGE: {
                if (!(packet instanceof MessagePacket)) return;
                MessagePacket messagePacket = (MessagePacket) packet;
                ProxiedPlayer player = plugin.getProxy().getPlayer(messagePacket.getUuid());
                if (player == null) return;
                for (String message : messagePacket.getMessages()) {
                    player.sendMessage(TextComponent.fromLegacyText(
                            ChatColor.translateAlternateColorCodes('&', message)
                    ));
                }
                break;
            }
            case LANGUAGES_UPDATE: {
                if (!(packet instanceof LanguagesUpdatePacket)) return;
                LanguagesUpdatePacket languagesUpdatePacket = (LanguagesUpdatePacket) packet;
                sender.sendPacketToAllExcept(languagesUpdatePacket, languagesUpdatePacket.getServer());
                break;
            }
        }
    }

    private boolean joinArena(JoinPacket joinPacket) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(joinPacket.getPlayer());
        if (proxiedPlayer == null) {
            return false;
        }
        if (joinPacket.isFirstJoin()) {
            plugin.getPlayerServers().put(joinPacket.getPlayer(), proxiedPlayer.getServer().getInfo().getName());
        }
        if (!joinPacket.isLocalJoin()) {
            if (proxiedPlayer.getServer().getInfo()
                    .getName().equals(joinPacket.getGame().getServer())) {
                return true;
            }
            ServerInfo info = plugin.getProxy().getServerInfo(joinPacket.getGame().getServer());
            if (info == null) {
                proxiedPlayer.sendMessage("Fuck off");
                return false;
            }
            proxiedPlayer.sendMessage("CONNECTING TO " + info.getName());
            proxiedPlayer.connect(info);
            return true;
        }

        return false;
    }

    private void sendBackToServer(BackToServerPacket backToServerPacket) {
        ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(backToServerPacket.getPlayer());
        if (proxiedPlayer == null) {
            return;
        }

        String firstChoiceServer;
        String secondChoiceServer;

        if (backToServerPacket.getServerPriority().equals(BackToServerPacket.ServerPriority.PREVIOUS)) {
            firstChoiceServer = plugin.getPlayerServers().get(proxiedPlayer.getName());
            secondChoiceServer = backToServerPacket.getLobby();
        } else {
            firstChoiceServer = backToServerPacket.getLobby();
            secondChoiceServer = plugin.getPlayerServers().get(proxiedPlayer.getName());
        }

        if (!connectToServer(proxiedPlayer, firstChoiceServer)) {
            connectToServer(proxiedPlayer, secondChoiceServer);
        }


        plugin.getPlayerServers().remove(backToServerPacket.getPlayer());
    }

    private boolean connectToServer(ProxiedPlayer player, String serverString) {
        if (serverString == null) {
            return false;
        }

        ServerInfo targetServer = plugin.getProxy().getServerInfo(serverString);

        if (targetServer == null) {
            return false;
        }

        if (player.getServer().getInfo().equals(targetServer)) {
            return true;
        }

        if (socketServer.getSpigotSocket().containsKey(serverString)) {
            player.connect(targetServer);
            return true;
        }


        return false;
    }


}
