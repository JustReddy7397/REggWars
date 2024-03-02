package ga.justreddy.wiki.reggwars.support.bungeemode.messenger.redis;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessengerReceiver;
import org.bukkit.Bukkit;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author JustReddy
 */
public class RedisMessengerReceiver implements IMessengerReceiver {

    private final RedisMessenger messenger;

    private final BinaryJedisPubSub pubSub;

    public RedisMessengerReceiver(RedisMessenger messenger) {
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

    }
}
