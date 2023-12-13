package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@Getter
public class GamePacket extends Packet implements Serializable {

    private final BungeeGame game;

    public GamePacket(PacketType packetType, BungeeGame game) {
        super(packetType);
        this.game = game;
    }
}
