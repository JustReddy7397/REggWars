package ga.justreddy.wiki.reggwars.support.bungeemode.messenger.rabbit;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessengerReceiver;
import redis.clients.jedis.JedisPubSub;

/**
 * @author JustReddy
 */
public class RabbitMessengerReceiver implements IMessengerReceiver {

    private final RabbitMessenger messenger;

    public RabbitMessengerReceiver(RabbitMessenger messenger) {
        this.messenger = messenger;

    }

    @Override
    public void handlePacket(Packet packet) {

    }
}
