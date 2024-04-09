package ga.justreddy.wiki.reggwars.socket;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.Core;
import ga.justreddy.wiki.reggwars.ServerMode;
import ga.justreddy.wiki.reggwars.config.SerializableConfig;
import ga.justreddy.wiki.reggwars.manager.ConfigManager;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.LanguageManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.packets.socket.Packet;
import ga.justreddy.wiki.reggwars.packets.socket.classes.*;
import ga.justreddy.wiki.reggwars.utils.JoinRunnable;
import org.bukkit.Bukkit;
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
public class SocketClientReceiver {

    private final REggWars plugin;
    private final SocketClient socketClient;

    public SocketClientReceiver(SocketClient socketClient) {
        plugin = REggWars.getInstance();
        this.socketClient = socketClient;
    }

    public void clientMessageReader(Socket socket, ObjectInputStream in) throws IOException {
        while (!socket.isClosed()) {
            try {
                Object object = in.readObject();
                if (!(object instanceof Packet)) return;
                Packet packet = (Packet) object;

                if (socketClient.isDebug()) {
                    socketClient.log(Level.INFO, "[X] Packet received: " + packet.getPacketType().toString());
                }

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> handlePacket(packet));

            } catch (ClassNotFoundException
                     | OptionalDataException
                     | StreamCorruptedException
                     | InvalidClassException e){
                if (socketClient.isDebug()) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void handlePacket(Packet packet) {
        switch (packet.getPacketType()) {
            case GAME_UPDATE: {
                GamePacket gamePacket = (GamePacket) packet;
                BungeeGame game = gamePacket.getGame();
                if (socketClient.isDebug()) {
                    socketClient.log(Level.INFO, "[X] Updating game: " + game.getName());
                }
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
                        break;
                    }
                }
                break;
            }
            case GAME_JOIN: {
                JoinPacket joinPacket = (JoinPacket) packet;
                new JoinRunnable().runTask(joinPacket.getPlayer(), joinPacket.getGame());
                break;
            }
            case GAME_LEAVE: {
                GamePlayerPacket playerPacket = (GamePlayerPacket) packet;
                Player player = Bukkit.getPlayerExact(playerPacket.getPlayer());
                if (player != null) {
                    GameManager.getManager().getGameByName(playerPacket.getGame().getName())
                            .onGamePlayerQuit(PlayerManager.getManager().getGamePlayer(player.getUniqueId()), true, false);
                }
                break;
            }
            case GAMES_ADD: {
                GamesPacket gamesPacket = (GamesPacket) packet;
                System.out.println("RECEIVED :D");
                addGames(gamesPacket.getGames());
                break;
            }
            case FORCE_START: {
                GamePlayerPacket playerPacket = (GamePlayerPacket) packet;
                // TODO LOL
                break;
            }
            case FORCE_STOP: {
                // TODO LOL
                break;
            }
            case KICK: {
                KickPacket kickPacket = (KickPacket) packet;
                Player player = Bukkit.getPlayerExact(kickPacket.getPlayer());
                if (player != null) {
                    if (socketClient.isDebug()) {
                        socketClient.log(Level.INFO, "[X] Kicking player: " + player.getName() + " from: " + kickPacket.getGame().getName());
                    }
                    GameManager.getManager().getGameByName(kickPacket.getGame().getName())
                            .onGamePlayerQuit(PlayerManager.getManager().getGamePlayer(player.getUniqueId()), false, false);
                }
            }
            case GAMES_SEND: {
                if (Core.MODE != ServerMode.LOBBY) return;
                if (!(packet instanceof GamesSendPacket)) return;
                GamesSendPacket sendPacket = (GamesSendPacket) packet;
                for (BungeeGame game : sendPacket.getGames()) {
                    if (socketClient.isDebug()) {
                        socketClient.log(Level.INFO, "[X] Adding game: " + game.getName());
                    }
                    GameManager.getManager().getBungeeGames().put(game.getName(), game);
                }
                break;
            }
            case CONFIG_UPDATE: {
                if (!(packet instanceof ConfigUpdatePacket)) return;
                ConfigUpdatePacket configUpdatePacket = (ConfigUpdatePacket) packet;
                SerializableConfig config = configUpdatePacket.getConfig();
                if (socketClient.isDebug()) {
                    socketClient.log(Level.INFO, "[X] Updating config " + config.getConfig());
                }
                ConfigManager.getManager().set(config);
                break;
            }
            case LANGUAGES_UPDATE: {
                if (!(packet instanceof LanguagesUpdatePacket)) return;
                LanguagesUpdatePacket languagesUpdatePacket = (LanguagesUpdatePacket) packet;
                Map<String, Map<String, Object>> languages = languagesUpdatePacket.getLanguages();
                if (socketClient.isDebug()) {
                    socketClient.log(Level.INFO, "[X] Updating languages " + languages.size());
                }
                for (Map.Entry<String, Map<String, Object>> entry : languages.entrySet()) {
                    LanguageManager.getManager().update(entry.getKey(), entry.getValue());
                }
                break;
            }
        }
    }

    private void addGames(List<BungeeGame> arenas) {
        if (Core.MODE == ServerMode.LOBBY) return;
        for (BungeeGame game : arenas) {
            GameManager.getManager().getBungeeGames().put(game.getName(), game);
        }
    }


}
