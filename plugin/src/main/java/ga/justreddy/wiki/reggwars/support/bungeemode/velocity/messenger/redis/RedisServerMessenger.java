package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.redis;

import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.messenger.IMessengerSender;
import ga.justreddy.wiki.reggwars.support.bungeemode.velocity.Velocity;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;

/**
 * @author JustReddy
 */
public class RedisServerMessenger implements IMessenger<Velocity> {

    @Getter
    private final String channel = "REggWarsBungee";

    @Getter
    private final byte[] channelBytes = channel.getBytes(StandardCharsets.UTF_8);

    private final JedisPool pool;
    private final Jedis jedis;

    private final IMessengerSender sender;
    private final IMessengerReceiver receiver;

    public RedisServerMessenger(String host, int port) {
        pool = new JedisPool(host, port);
        jedis = pool.getResource();
        sender = new RedisServerMessengerSender(this);
        receiver = new RedisServerMessengerReceiver(this);
    }

    @Override
    public void setup() {
        connect();
    }

    @Override
    public void connect() {
        jedis.connect();
    }

    @Override
    public void close() {
        jedis.disconnect();
    }

    @Override
    public void receive() {

    }

    @Override
    public Velocity getPlugin() {
        return Velocity.getInstance();
    }

    @Override
    public IMessengerSender getSender() {
        return sender;
    }

    @Override
    public IMessengerReceiver getReceiver() {
        return receiver;
    }

    public Jedis getJedis() {
        return jedis;
    }
}
