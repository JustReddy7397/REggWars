package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger;

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
