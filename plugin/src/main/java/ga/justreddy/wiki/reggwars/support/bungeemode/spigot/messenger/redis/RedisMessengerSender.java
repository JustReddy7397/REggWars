package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.redis;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerSender;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.ObjectOutputStream;

/**
 * @author JustReddy
 */
public class RedisMessengerSender implements IMessengerSender {

    private final RedisMessenger messenger;

    public RedisMessengerSender(RedisMessenger messenger) {
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
}
