package ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.socket;

import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.WorldManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.PacketType;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;
import ga.justreddy.wiki.reggwars.socket.SocketClient;
import ga.justreddy.wiki.reggwars.support.bungeemode.spigot.messenger.IMessengerReceiver;
import ga.justreddy.wiki.reggwars.utils.JoinRunnable;
import ga.justreddy.wiki.reggwars.utils.Util;
import ga.justreddy.wiki.reggwars.utils.world.BukkitWorldHasher;
import ga.justreddy.wiki.reggwars.utils.world.SlimeWorldHasher;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author JustReddy
 */
public class SocketClientMessengerReceiver implements IMessengerReceiver {

    private final REggWars plugin;
    private final SocketClientMessenger socketClient;

    public SocketClientMessengerReceiver(SocketClientMessenger socketClient) {
        plugin = REggWars.getInstance();
        this.socketClient = socketClient;
    }

    public void clientMessageReader(Socket socket, ObjectInputStream in) throws IOException {
        while (!socket.isClosed()) {
            try {
                Object object = in.readObject();
                if (!(object instanceof Packet)) return;
                Packet packet = (Packet) object;


                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> handlePacket(packet));

            } catch (ClassNotFoundException
                     | OptionalDataException
                     | StreamCorruptedException
                     | InvalidClassException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet.getServer().equalsIgnoreCase(REggWars.getInstance().getServerName()) && packet.getPacketType() != PacketType.GAME_UPDATE) {
            return;
        }
        switch (packet.getPacketType()) {
            case GAME_UPDATE: {
                GamePacket gamePacket = (GamePacket) packet;
                BungeeGame game = gamePacket.getGame();
                GameManager.getManager().getBungeeGames().put(game.getName(), game);
                break;
            }
            case GAME_REMOVE: {
                GamePacket gamePacket = (GamePacket) packet;
                BungeeGame game = gamePacket.getGame();
                GameManager.getManager().getBungeeGames().remove(game.getName());
                break;
            }
            case SERVER_GAMES_ADD: {
                ServerGamesPacket serverGamesPacket = (ServerGamesPacket) packet;
                addGames(serverGamesPacket.getGames());
                break;
            }
            case SERVER_GAMES_REMOVE: {
                StringPacket stringPacket = (StringPacket) packet;
                Map<String, BungeeGame> games = GameManager.getManager().getBungeeGames();
                for (String game : new ArrayList<>(games.keySet())) {
                    if (games.get(game).getServer().equalsIgnoreCase(stringPacket.getString())) {
                        games.remove(game);
                    }
                }
                break;
            }
            /*case GAMES_REMOVE: {
                ServerGamesPacket serverGamesPacket = (ServerGamesPacket) packet;
                for (BungeeGame game : serverGamesPacket.getGames()) {
                    GameManager.getManager().getBungeeGames().remove(game.getName());
                }
                break;
            }*/
            case GAME_JOIN: {
                if (Core.MODE == ServerMode.LOBBY) return;
                JoinPacket joinPacket = (JoinPacket) packet;
                if (joinPacket.isLocalJoin()) {
                    new JoinRunnable().runTask(joinPacket.getPlayer(), joinPacket.getGame());
                    return;
                }
                GameManager.getManager().getPlayerQueues().put(joinPacket.getUuid(), joinPacket.getGame().getName());                break;
            }
            case GAME_LEAVE: {
                GamePlayerPacket playerPacket = (GamePlayerPacket) packet;
                Player player = Bukkit.getPlayerExact(playerPacket.getPlayer());
                if (player != null) {
                    GameManager.getManager().getGameByName(playerPacket.getGame().getName())
                            .onGamePlayerQuit(GamePlayer.get(player), true, false);
                }
                break;
            }
            case GAMES_ADD: {
                GamesPacket gamesPacket = (GamesPacket) packet;
                addGames(gamesPacket.getGames());
                break;
            }
            case GAME_ADD:
                if (!(packet instanceof GameAddPacket)) return;
                GameAddPacket gameAddPacket = (GameAddPacket) packet;
                if (gameAddPacket.getSlimeHasher() != null) {
                    handleSlimeGameAdd(gameAddPacket);
                } else {
                    handleBukkitGameAdd(gameAddPacket);
                }
                break;
            /*case GAME_ADD: {
                if (!(packet instanceof GameAddPacket)) return;
                GameAddPacket gameAddPacket = (GameAddPacket) packet;
                String name = gameAddPacket.getGameName();
                Map<String, Object> obj = gameAddPacket.getObjects();
                SlimeWorldHasher hasher = gameAddPacket.getHasher();
                File gameFile = new File(GameManager.getManager().getGamesFolder() + "/" + name + ".yml");
                try {
                    if (!gameFile.exists()) gameFile.createNewFile();
                    FileConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
                    for (Map.Entry<String, Object> entry : obj.entrySet()) {
                        config.set(entry.getKey(), entry.getValue());
                    }
                    config.save(gameFile);
                    boolean enabled = config.getBoolean("settings.enabled");
                    File worldFile = Util.downloadWorld(hasher);
                    WorldManager.getManager().copySlimeWorld(worldFile.getName().replace(".slime", ""));
                    Game game = GameManager.getManager().register(name, config);
                    if (enabled) {
                        BungeeGame bungeeGame = new BungeeGame(
                                game.getName(),
                                BedFight.getInstance().getServerName(),
                                game.getGameState(),
                                game.getGameType(),
                                game.getPlayersWithNames(),
                                game.getMaximum(),
                                false
                        );
                        sendUpdateGamePacket(bungeeGame);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }*/
            case KICK: {
                KickPacket kickPacket = (KickPacket) packet;
                Player player = Bukkit.getPlayerExact(kickPacket.getPlayer());
                if (player != null) {
                    GameManager.getManager().getGameByName(kickPacket.getGame().getName())
                            .onGamePlayerQuit(GamePlayer.get(player), true, false);
                }
            }
            case GAMES_SEND: {
                if (Core.MODE != ServerMode.LOBBY) return;
                if (!(packet instanceof GamesSendPacket)) return;
                GamesSendPacket sendPacket = (GamesSendPacket) packet;
                System.out.println("Received games from " + sendPacket.getServer());
                for (BungeeGame game : sendPacket.getGames()) {
                    GameManager.getManager().getBungeeGames().put(game.getName(), game);
                }
                break;
            }
            case CONFIG_UPDATE: {
                if (!(packet instanceof ConfigUpdatePacket)) return;
                ConfigUpdatePacket configUpdatePacket = (ConfigUpdatePacket) packet;
                SerializableConfig config = configUpdatePacket.getConfig();
                /*ConfigManager.getManager().set(config);*/
                // TODO
                break;
            }
            /*case MAP_ADD: {
                if (!(packet instanceof MapUpdatePacket)) return;
                MapUpdatePacket mapPacket = (MapUpdatePacket) packet;
                RotationManager.getManager().
                        addBungee(mapPacket.getGameName());
                break;
            }
            case MAP_REMOVE: {
                if (!(packet instanceof MapUpdatePacket)) return;
                MapUpdatePacket mapPacket = (MapUpdatePacket) packet;
                RotationManager.getManager().
                        removeBungee(mapPacket.getGameName());
                break;
            }*/
            case GAMES_REQUEST: {
                System.out.println("Received request from " + packet.getServer());
                List<BungeeGame> games = new ArrayList<>(GameManager.getManager().getBungeeGames().values());
                System.out.println(games);
                socketClient.getSender().sendGamesPacket(REggWars.getInstance().getServerName(), games);
                break;
            }
        }
    }



    @SneakyThrows
    private void handleSlimeGameAdd(GameAddPacket gameAddPacket) {
        String name = gameAddPacket.getGameName();
        Map<String, Object> obj = gameAddPacket.getObjects();
        SlimeWorldHasher hasher = gameAddPacket.getSlimeHasher();
        File gameFile = new File(GameManager.getManager().getGamesFolder() + "/" + name + ".yml");
        if (!gameFile.exists()) gameFile.createNewFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        config.save(gameFile);
        boolean enabled = config.getBoolean("settings.enabled");
        File worldFile = Util.downloadWorldSlime(hasher);
        WorldManager.getManager().copySlimeWorld(worldFile.getName().replace(".slime", ""));
        IGame game = GameManager.getManager().register(name, config);
        if (enabled) {
            Util.updateGame(game);
        }
    }

    @SneakyThrows
    private void handleBukkitGameAdd(GameAddPacket gameAddPacket) {
        String name = gameAddPacket.getGameName();
        Map<String, Object> obj = gameAddPacket.getObjects();
        BukkitWorldHasher hasher = gameAddPacket.getBukkitHasher();
        File gameFile = new File(GameManager.getManager().getGamesFolder() + "/" + name + ".yml");
        if (!gameFile.exists()) gameFile.createNewFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(gameFile);
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        config.save(gameFile);

        boolean enabled = config.getBoolean("settings.enabled");
        File worldFile = Util.downloadWorldBukkit(hasher);
        WorldManager.getManager().copyWorld(worldFile.getName());
        IGame game = GameManager.getManager().register(name, config);
        if (enabled) {
            Util.updateGame(game);
        }
    }

    private void addGames(List<BungeeGame> arenas) {
        if (Core.MODE == ServerMode.LOBBY) return;
        for (BungeeGame game : arenas) {
            GameManager.getManager().getBungeeGames().put(game.getName(), game);
        }
    }

}
