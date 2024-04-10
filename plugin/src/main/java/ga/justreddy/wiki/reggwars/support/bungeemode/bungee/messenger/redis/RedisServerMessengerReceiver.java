package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.redis;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.classes.BackToServerPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.JoinPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.MessagePacket;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.utils.iridium.IridiumColorAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.BinaryJedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author JustReddy
 */
public class RedisServerMessengerReceiver implements IMessengerReceiver {


    private final RedisServerMessenger messenger;

    private final BinaryJedisPubSub pubSub;

    public RedisServerMessengerReceiver(RedisServerMessenger messenger) {
        this.messenger = messenger;
        this.pubSub = new BinaryJedisPubSub() {
            @Override
            public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
                String channelString = new String(channel, StandardCharsets.UTF_8);
                if (!channelString.equalsIgnoreCase(messenger.getChannel())) return;
                try (ByteArrayInputStream bis = new ByteArrayInputStream(message);
                     ObjectInputStream ois = new ObjectInputStream(bis)
                ) {
                    Object o = ois.readObject();
                    if (!(o instanceof Packet)) return;
                    Packet packet = (Packet) o;
                    handlePacket(packet);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        subscribe();
    }

    public void subscribe() {
        messenger.getJedis().subscribe(pubSub, messenger.getChannelBytes());
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
                ProxiedPlayer proxiedPlayer = Bungee.getInstance().getProxy().getPlayer(messagePacket.getUuid());
                if (proxiedPlayer == null) {
                    return;
                }
                for (String message : messagePacket.getMessages()) {
                    proxiedPlayer.sendMessage(IridiumColorAPI.process(message));
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
                    messenger.getSender().sendPacket(packet);
                    break;
                }
            }
        }

    }

    private boolean joinArena(JoinPacket joinPacket) {
        ProxiedPlayer proxiedPlayer = Bungee.getInstance().getProxy().getPlayer(joinPacket.getPlayer());
        if (proxiedPlayer == null) {
            return false;
        }
        if (joinPacket.isFirstJoin()) {
            Bungee.getInstance().getPlayerServers().put(joinPacket.getPlayer(), proxiedPlayer.getServer().getInfo().getName());
        }
        if (!joinPacket.isLocalJoin()) {
            if (proxiedPlayer.getServer().getInfo()
                    .getName().equals(joinPacket.getGame().getServer())) {
                return true;
            }
            ServerInfo info = Bungee.getInstance().getProxy().getServerInfo(joinPacket.getGame().getServer());
            if (info == null) {
                if (!Bungee.getInstance().isEmptyString("messages.no-game-found")) {
                    proxiedPlayer.sendMessage(new TextComponent(IridiumColorAPI.process(Bungee.getInstance().getBungeeConfig()
                            .getConfig().getString("messages.no-game-found").replaceAll("<type>", joinPacket.getGame().getGameMode().getName()))));
                }
                return false;
            }
            if (proxiedPlayer.getServer().getInfo().getName().equals(info.getName())) return true;
            if (!Bungee.getInstance().isEmptyString("messages.connecting")) {
                proxiedPlayer.sendMessage(new TextComponent(IridiumColorAPI.process(Bungee.getInstance().getBungeeConfig()
                        .getConfig().getString("messages.connecting").replaceAll("<type>", joinPacket.getGame().getGameMode().getName()))));
            }
            proxiedPlayer.connect(info);
            return true;
        }

        return false;
    }

    private void sendBackToServer(BackToServerPacket backToServerPacket) {
        ProxiedPlayer proxiedPlayer = Bungee.getInstance().getProxy().getPlayer(backToServerPacket.getPlayer());
        if (proxiedPlayer == null) {
            return;
        }

        String firstChoiceServer;
        String secondChoiceServer;

        if (backToServerPacket.getServerPriority().equals(BackToServerPacket.ServerPriority.PREVIOUS)) {
            firstChoiceServer = Bungee.getInstance().getPlayerServers().get(proxiedPlayer.getName());
            secondChoiceServer = backToServerPacket.getLobby();
        } else {
            firstChoiceServer = backToServerPacket.getLobby();
            secondChoiceServer = Bungee.getInstance().getPlayerServers().get(proxiedPlayer.getName());
        }

        if (!connectToServer(proxiedPlayer, firstChoiceServer)) {
            connectToServer(proxiedPlayer, secondChoiceServer);
        }

        Bungee.getInstance().getPlayerServers().remove(backToServerPacket.getPlayer());
    }

    private boolean connectToServer(ProxiedPlayer player, String serverString) {
        if (serverString == null) {
            return false;
        }

        ServerInfo targetServer = Bungee.getInstance().getProxy().getServerInfo(serverString);

        if (targetServer == null) {
            return false;
        }

        if (player.getServer().getInfo().equals(targetServer)) {
            return true;
        }

        player.connect(targetServer);

        return true;
    }
}
