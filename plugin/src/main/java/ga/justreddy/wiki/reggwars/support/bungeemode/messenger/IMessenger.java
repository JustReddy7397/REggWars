package ga.justreddy.wiki.reggwars.support.bungeemode.messenger;

import org.bukkit.plugin.Plugin;

/**
 * @author JustReddy
 */
public interface IMessenger<T> {

    void setup();

    void connect();

    void close();

    void receive();

    T getPlugin();

    IMessengerSender getSender();

    IMessengerReceiver getReceiver();

}
