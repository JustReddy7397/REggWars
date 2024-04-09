package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeWorldHasher;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;

import java.io.Serializable;
import java.util.Map;

/**
 * @author JustReddy
 */
public class GameAddPacket extends Packet implements Serializable {

    private final String server;
    private final String gameName;
    private final Map<String, Object> objects;
    private final SlimeWorldHasher hasher;

    public GameAddPacket(String server, String gameName, Map<String, Object> objects, SlimeWorldHasher hasher) {
        super(PacketType.GAME_ADD);
        this.server = server;
        this.gameName = gameName;
        this.objects = objects;
        this.hasher = hasher;
    }
    public GameAddPacket(String gameName, Map<String, Object> objects, SlimeWorldHasher hasher) {
        super(PacketType.GAME_ADD);
        this.server = REggWars.getInstance().getServerName();
        this.gameName = gameName;
        this.objects = objects;
        this.hasher = hasher;
    }

}
