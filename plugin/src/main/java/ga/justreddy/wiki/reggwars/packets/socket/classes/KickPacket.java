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
public class KickPacket extends Packet implements Serializable {

    private final BungeeGame game;
    private final String player;
    private final String target;

    public KickPacket(String player, String target, BungeeGame game) {
        super(PacketType.KICK);
        this.game = game;
        this.player = player;
        this.target = target;
    }

}
