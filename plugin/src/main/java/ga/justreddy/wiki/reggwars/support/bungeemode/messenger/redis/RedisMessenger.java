package ga.justreddy.wiki.reggwars.support.bungeemode.messenger.redis;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessengerSender;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;

/**
 * @author JustReddy
 */
@Getter
public class RedisMessenger implements IMessenger<REggWars> {

    private final String channel = "REggWars";

    private final byte[] channelBytes = channel.getBytes(StandardCharsets.UTF_8);

    private final JedisPool pool;
    private final Jedis jedis;

    private final RedisMessengerSender sender;
    private final RedisMessengerReceiver receiver;

    public RedisMessenger(String host, int port, String clientName) {
        pool = new JedisPool(host, port);
        jedis = pool.getResource();
        this.sender = new RedisMessengerSender(this);
        this.receiver = new RedisMessengerReceiver(this);
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
    public REggWars getPlugin() {
        return null;
    }

    @Override
    public IMessengerSender getSender() {
        return null;
    }

    @Override
    public IMessengerReceiver getReceiver() {
        return null;
    }
}
