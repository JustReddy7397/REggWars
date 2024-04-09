package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.rabbit;

import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerSender;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author JustReddy
 */
public class RabbitMessengerSender implements IMessengerSender {

    private final RabbitMessenger messenger;

    public RabbitMessengerSender(RabbitMessenger messenger) {
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
    @Override
    public void sendPacket(Packet packet) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(packet);
            oos.flush();
            byte[] data = bos.toByteArray();
            publish(data);
        }
    }
}


