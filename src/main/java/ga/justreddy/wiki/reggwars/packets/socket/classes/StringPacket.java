package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@Getter
public class StringPacket extends Packet implements Serializable {

    private final String string;

    public StringPacket(PacketType packetType, String string) {
        super(packetType);
        this.string = string;
    }
}
