package ga.justreddy.wiki.reggwars.packets.socket.classes;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
@Getter
public class MessagePacket extends Packet implements Serializable {

    private final UUID uuid;
    private final List<String> messages;

    public MessagePacket(UUID uuid, List<String> messages) {
        super(PacketType.MESSAGE);
        this.uuid = uuid;
        this.messages = messages;
    }

    public MessagePacket(UUID uuid, String... messages) {
        this(uuid, Arrays.asList(messages));
    }

}
