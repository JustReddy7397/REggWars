package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@Getter
public class ConfigUpdatePacket extends Packet implements Serializable {

    private final String server;
    private final SerializableConfig config;

    public ConfigUpdatePacket(String server, SerializableConfig config) {
        super(PacketType.CONFIG_UPDATE);
        this.server = server;
        this.config = config;
    }
}
