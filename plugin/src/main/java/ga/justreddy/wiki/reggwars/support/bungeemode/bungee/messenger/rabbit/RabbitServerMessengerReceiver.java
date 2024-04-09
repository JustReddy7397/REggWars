package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.rabbit;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerReceiver;

/**
 * @author JustReddy
 */
public class RabbitServerMessengerReceiver implements IMessengerReceiver {

    private final RabbitServerMessenger messenger;

    public RabbitServerMessengerReceiver(RabbitServerMessenger messenger) {
        this.messenger = messenger;

    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet.getServer().equalsIgnoreCase("REggWarsBungee")) {
            return;
        }



    }

}
