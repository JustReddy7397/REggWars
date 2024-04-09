package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;

/**
 * @author JustReddy
 */
public interface IMessengerReceiver {

    void handlePacket(Packet packet);

}
