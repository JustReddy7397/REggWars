package ga.justreddy.wiki.reggwars.support.bungeemode.messenger;

import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;

import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface IMessengerSender {

    void sendPacket(Packet packet);

    default void sendJoinPacket(BungeeGame game, String player, boolean joinServer, boolean localJoin) {
        JoinPacket joinPacket = new JoinPacket(game, player, joinServer, localJoin);
        sendPacket(joinPacket);
    }

    default void sendGamesPacket(String server, List<BungeeGame> games) {
        ServerGamesPacket serverGamesPacket = new ServerGamesPacket(PacketType.SERVER_GAMES_ADD, server, games);
        sendPacket(serverGamesPacket);
    }

    default void sendRequestPacket(String server) {
        GameRequestPacket requestPacket = new GameRequestPacket(server);
        sendPacket(requestPacket);
    }

    default void sendUpdateGamePacket(BungeeGame games) {
        GamePacket gamePacket = new GamePacket(PacketType.GAME_UPDATE, games);
        sendPacket(gamePacket);
    }

    default void sendRemoveGamePacket(BungeeGame game) {
        GamePacket gamePacket = new GamePacket(PacketType.GAME_REMOVE, game);
        sendPacket(gamePacket);
    }

    default void sendBackToServerPacket(BackToServerPacket.ServerPriority serverPriority, String player, String lobby) {
        BackToServerPacket backToServerPacket = new BackToServerPacket(serverPriority, player, lobby);
        sendPacket(backToServerPacket);
    }

    default void sendForceStartPacket(String player, BungeeGame game) {
        GamePlayerPacket gamePlayerPacket = new GamePlayerPacket(PacketType.FORCE_START, game, player);
        sendPacket(gamePlayerPacket);
    }

    default void sendForceStopPacket(String player, BungeeGame game) {
        GamePlayerPacket arenaPlayerPacket = new GamePlayerPacket(PacketType.FORCE_STOP, game, player);
        sendPacket(arenaPlayerPacket);
    }

    default void sendKickPacket(String player, String target, BungeeGame game) {
        KickPacket kickPacket = new KickPacket(player, target, game);
        sendPacket(kickPacket);
    }

    default void sendConfigUpdatePacket(String server, Config config) {
        ConfigUpdatePacket configUpdatePacket = new ConfigUpdatePacket(server, new SerializableConfig(config));
        sendPacket(configUpdatePacket);
    }

    default void sendMessagePacket(UUID uuid, List<String>  messages) {
        MessagePacket messagePacket = new MessagePacket(uuid, messages);
        sendPacket(messagePacket);
    }

    default void sendMessagePacket(UUID uuid, String... message) {
        MessagePacket messagePacket = new MessagePacket(uuid, message);
        sendPacket(messagePacket);
    }

    default void sendLanguagesUpdatePacket() {
        LanguagesUpdatePacket languagesUpdatePacket = new LanguagesUpdatePacket();
        sendPacket(languagesUpdatePacket);
    }

}
