package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@Getter
public class BackToServerPacket extends Packet implements Serializable {

    private final String player;
    private final String lobby;
    private final ServerPriority serverPriority;

    public BackToServerPacket(ServerPriority serverPriority, String player, String lobby) {
        super(PacketType.BACK_TO_SERVER);
        this.serverPriority = serverPriority;
        this.player = player;
        this.lobby = lobby;
    }

    public enum ServerPriority {LOBBY, PREVIOUS}

}
