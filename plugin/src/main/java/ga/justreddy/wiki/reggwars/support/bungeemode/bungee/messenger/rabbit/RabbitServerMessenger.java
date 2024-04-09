package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger.IMessengerSender;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * @author JustReddy
 */
public class RabbitServerMessenger implements IMessenger<Bungee> {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String vhost = "/";

    @Getter
    private final Connection connection;
    @Getter
    @Setter
    private Channel channel;
    private final Set<String> sentData = new HashSet<>();
    private final String tag = "REggWars";

    private final IMessengerReceiver receiver;
    private final IMessengerSender sender;

    public RabbitServerMessenger(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to RabbitMQ", e);
        }
        receiver = new RabbitServerMessengerReceiver(this);
        sender = new RabbitServerMessengerSender(this);
    }

    public RabbitServerMessenger(String host, int port, String username, String password, String vhost) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(vhost);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to RabbitMQ", e);
        }
        receiver = new RabbitServerMessengerReceiver(this);
        sender = new RabbitServerMessengerSender(this);
    }

    @SneakyThrows
    @Override
    public void setup() {
        channel.exchangeDeclare(tag, "direct", true);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, tag, "info");
        DeliverCallback callback = (x, delivery) -> {
            handle(tag, delivery.getBody());
        };
        channel.basicConsume(queueName, true, callback, x -> {});
    }

    @Override
    public void connect() {

    }

    @SneakyThrows
    @Override
    public void close() {
        channel.close();
        connection.close();
    }

    @Override
    public void receive() {

    }

    private void handle(String channel, byte[] data) {
        if (!Objects.equals(channel, tag)) return;
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(stream)
        ) {
            Object obj = in.readObject();
            if (!(obj instanceof Packet)) return;
            Packet packet = (Packet) obj;
            getReceiver().handlePacket(packet);
        }catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bungee getPlugin() {
        return Bungee.getInstance();
    }

    @Override
    public IMessengerSender getSender() {
        return sender;
    }

    @Override
    public IMessengerReceiver getReceiver() {
        return receiver;
    }


}
