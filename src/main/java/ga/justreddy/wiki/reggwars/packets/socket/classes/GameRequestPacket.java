package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@Getter
public class GameRequestPacket extends Packet implements Serializable {

    private final String server;

    public GameRequestPacket(String server) {
        super(PacketType.GAMES_REQUEST);
        this.server = server;
    }

}
