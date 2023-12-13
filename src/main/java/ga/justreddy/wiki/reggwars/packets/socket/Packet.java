package ga.justreddy.wiki.reggwars.packets.socket;

import java.io.Serializable;

/**
 * @author JustReddy
 */
public class Packet implements Serializable {

    private PacketType packetType;

    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

}
