package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;
import ga.justreddy.wiki.reggwars.utils.Util;
import ga.justreddy.wiki.reggwars.utils.world.BukkitWorldHasher;
import ga.justreddy.wiki.reggwars.utils.world.SlimeWorldHasher;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface IMessengerSender {

    void sendPacket(Packet packet);

    default void sendJoinPacket(BungeeGame game, UUID uuid, String player, boolean joinServer, boolean localJoin) {
        JoinPacket joinPacket = new JoinPacket(game, uuid, player, joinServer, localJoin);
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

    default void sendGameAddPacket(Game game) {
        String name = game.getName();
        FileConfiguration config = game.getConfig();
        Map<String, Object> obj = new HashMap<>();
        // So scuffed holy shit
        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            for (String key2 : section.getKeys(false)) {
                if (section.get(key2) instanceof MemorySection) {
                    ConfigurationSection sec3 = section.getConfigurationSection(key2);
                    for (String key3 : sec3.getKeys(false)) {
                        obj.put(key + "." + key2 + "." + key3, sec3.get(key3));
                    }
                } else {
                    obj.put(key + "." + key2, section.get(key2));
                }
            }
        }

        if (REggWars.getInstance().isSlimeEnabled()) {
            File file = new File("slime_worlds/" + name + ".slime");
            SlimeWorldHasher hasher = Util.hashWorldSlime(file);
            sendGameAddPacket(REggWars.getInstance().getServerName(),
                    game.getName(), obj, hasher);
        } else {
            File worldFolder = new File(game.getName());
            BukkitWorldHasher hasher = Util.hashWorldBukkit(worldFolder);
            sendGameAddPacket(REggWars.getInstance().getServerName(),
                    game.getName(), obj, hasher);
        }

    }

    default void sendGameAddPacket(String server, String gameName, Map<String, Object> objects, SlimeWorldHasher hasher) {
        GameAddPacket gameAddPacket = GameAddPacket.createSlime(server, gameName, objects, hasher);
        sendPacket(gameAddPacket);
    }

    default void sendGameAddPacket(String server, String gameName, Map<String, Object> objects, BukkitWorldHasher hasher) {
        GameAddPacket gameAddPacket = GameAddPacket.createBukkit(server, gameName, objects, hasher);
        sendPacket(gameAddPacket);
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
