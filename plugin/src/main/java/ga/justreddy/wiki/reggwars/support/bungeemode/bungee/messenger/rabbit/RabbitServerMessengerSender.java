package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.rabbit;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerSender;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.rabbit.RabbitMessenger;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

/**
 * @author JustReddy
 */
public class RabbitServerMessengerSender implements IMessengerSender {

    private final RabbitServerMessenger messenger;

    public RabbitServerMessengerSender(RabbitServerMessenger messenger) {
        this.messenger = messenger;
    }

    private void publish(byte[] data) {
        try {
            if (!messenger.getChannel().isOpen()) {
                messenger.setChannel(messenger.getConnection().createChannel());
                messenger.setup();
            }
            messenger.getChannel().basicPublish("REggWars", "info", null, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void sendPacket(Packet packet) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(packet);
            oos.flush();
            byte[] data = bos.toByteArray();
            publish(data);
        }
    }

    @Override
    public void sendPacket(Packet packet, String server) {
        sendPacket(packet);
    }

    @Override
    public void sendPacketToServer(Packet packet, String server) {
        throw new UnsupportedOperationException("Not Supported on BungeeCord");
    }

    @Override
    public void sendPacketToAllExcept(Packet packet, String exceptServer) {
        throw new UnsupportedOperationException("Not Supported on BungeeCord");

    }

    @Override
    public void sendPacketToAll(Packet packet) {
        throw new UnsupportedOperationException("Not Supported on BungeeCord");

    }

    @Override
    public Set<String> getServersExcept(String server) {
        throw new UnsupportedOperationException("Not Supported on BungeeCord");
    }
}
