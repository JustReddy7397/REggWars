package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.utils.world.BukkitWorldHasher;
import ga.justreddy.wiki.reggwars.utils.world.SlimeWorldHasher;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author JustReddy
 */
@Getter
public class GameAddPacket extends Packet implements Serializable {

    private final String server;
    private final  String gameName;
    private final Map<String, Object> objects;
    private final SlimeWorldHasher slimeHasher;
    private final BukkitWorldHasher bukkitHasher;

    private GameAddPacket(String server, String gameName, Map<String, Object> objects, SlimeWorldHasher slimeHasher) {
        super(PacketType.GAME_ADD);
        this.server = server;
        this.gameName = gameName;
        this.objects = objects;
        this.slimeHasher = slimeHasher;
        this.bukkitHasher = null;
    }

    private GameAddPacket(String server, String gameName, Map<String, Object> objects, BukkitWorldHasher bukkitHasher) {
        super(PacketType.GAME_ADD);
        this.server = server;
        this.gameName = gameName;
        this.objects = objects;
        this.slimeHasher = null;
        this.bukkitHasher = bukkitHasher;
    }

    public static GameAddPacket createSlime(String server, String gameName, Map<String, Object> objects, SlimeWorldHasher slimeHasher) {
        return new GameAddPacket(server, gameName, objects, slimeHasher);
    }

    public static GameAddPacket createBukkit(String server, String gameName, Map<String, Object> objects, BukkitWorldHasher bukkitHasher) {
        return new GameAddPacket(server, gameName, objects, bukkitHasher);
    }

}
