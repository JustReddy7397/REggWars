package ga.justreddy.wiki.reggwars.packets.socket;

import ga.justreddy.wiki.reggwars.REggWars;

import java.io.Serializable;

/**
 * @author JustReddy
 */
public class Packet implements Serializable {

    private PacketType packetType;
    private String server = REggWars.getInstance().getServerName();

    public Packet(PacketType packetType) {
        this.packetType = packetType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }
}
