package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.redis;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerSender;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author JustReddy
 */
public class RedisServerMessengerSender implements IMessengerSender {


    private final RedisServerMessenger messenger;

    public RedisServerMessengerSender(RedisServerMessenger messenger) {
        this.messenger = messenger;
    }

    @Override
    public void sendPacket(Packet packet) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(packet);
            oos.flush();
            byte[] data = bos.toByteArray();
            messenger.getJedis().publish(messenger.getChannelBytes(), data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPacket(Packet packet, String server) {
        sendPacket(packet);
    }

    @Override
    public void sendPacketToServer(Packet packet, String server) {
        sendPacket(packet);
    }

    @Override
    public void sendPacketToAllExcept(Packet packet, String exceptServer) {
        sendPacket(packet);
    }

    @Override
    public void sendPacketToAll(Packet packet) {
        sendPacket(packet);
    }

    @Override
    public Set<String> getServersExcept(String server) {
        return new HashSet<>();
    }
}
