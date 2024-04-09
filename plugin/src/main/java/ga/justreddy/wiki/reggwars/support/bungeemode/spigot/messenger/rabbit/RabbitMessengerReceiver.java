package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.rabbit;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerReceiver;

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
