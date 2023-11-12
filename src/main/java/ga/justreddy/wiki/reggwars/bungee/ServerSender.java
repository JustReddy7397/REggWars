package ga.justreddy.wiki.reggwars.bungee;

/**
 * @author JustReddy
 */
public abstract class ServerSender {

    protected Object sender;

    public ServerSender(Object sender) {
        this.sender = sender;
    }

    public abstract void sendMessage(String message);


}
