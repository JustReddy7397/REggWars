package ga.justreddy.wiki.reggwars.packets.socket;


import java.util.Set;

/**
 * @author JustReddy
 */
public class TargetPacket {

    private final Packet packet;
    private final Set<String> servers;

    public TargetPacket(Packet packet, Set<String> servers) {
        this.packet = packet;
        this.servers = servers;
    }

    public Packet getPacket() {
        return packet;
    }

    public Set<String> getServers() {
        return servers;
    }


}
