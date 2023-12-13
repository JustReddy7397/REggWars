package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author JustReddy
 */
@Getter
public class GamesPacket extends Packet implements Serializable {

    private final List<BungeeGame> games;

    public GamesPacket(PacketType packetType, List<BungeeGame> games) {
        super(packetType);
        this.games = games;
    }
}
