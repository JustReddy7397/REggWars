package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author JustReddy
 */
@Getter
public class GamesSendPacket extends Packet implements Serializable {

    private final String server;
    private final List<BungeeGame> games;

    public GamesSendPacket(String server,List<BungeeGame> games) {
        super(PacketType.GAMES_SEND);
        this.server = server;
        this.games = games;
    }
}
