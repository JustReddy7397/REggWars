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
public class JoinPacket extends Packet implements Serializable {

    private final BungeeGame game;
    private final String player;
    private final boolean firstJoin;
    private final boolean localJoin;

    public JoinPacket(BungeeGame game, String player, boolean firstJoin, boolean localJoin) {
        super(PacketType.GAME_JOIN);
        this.game = game;
        this.player = player;
        this.firstJoin = firstJoin;
        this.localJoin = localJoin;
    }

}
