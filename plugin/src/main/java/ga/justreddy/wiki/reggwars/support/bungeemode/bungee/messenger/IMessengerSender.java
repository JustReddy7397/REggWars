package ga.justreddy.wiki.reggwars.support.bungeemode.bungee.messenger;

import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.TargetPacket;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;

import java.util.*;

/**
 * @author JustReddy
 */
public interface IMessengerSender {

    void sendPacket(Packet packet, String server);

    void sendPacketToServer(Packet packet, String server);

    void sendPacketToAllExcept(Packet packet, String exceptServer);

    void sendPacketToAll(Packet packet);

    default void sendAllGames(String server, List<BungeeGame> games) {
        GamesPacket gamesPacket = new GamesPacket(PacketType.GAMES_ADD, games);
        sendPacketToServer(gamesPacket, server);
    }

    default void sendRemoveServerGames(String server) {
        StringPacket stringPacket = new StringPacket(PacketType.SERVER_GAMES_REMOVE, server);
        sendPacketToAllExcept(stringPacket, server);
    }

    Set<String> getServersExcept(String server);

    default void sendGamesPacket(String server, List<BungeeGame> bungeeGames) {
        GamesSendPacket packet = new GamesSendPacket(server, bungeeGames);
        sendPacketToServer(packet, server);
    }

}
