package ga.justreddy.wiki.reggwars.board;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.model.creator.BoardCreator;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author JustReddy
 */
public class EggWarsBoard {

    private static EggWarsBoard manager;

    public static EggWarsBoard getManager() {
        return manager == null ? manager = new EggWarsBoard() : manager;
    }

    private final Map<UUID, Integer> data;
    private final Map<UUID, BoardCreator> boards;

    private EggWarsBoard() {
        this.data = new HashMap<>();
        this.boards = new HashMap<>();
    }

    public void setLobbyBoard(IGamePlayer player) {
        if (player == null) return;
        removeScoreboard(player);

    }

    public void setGameBoard(IGamePlayer player) {
        if (player == null) return;
        removeScoreboard(player);
        ILanguage language = player.getSettings().getLanguage();
        FileConfiguration config = language.getConfig();
        ConfigurationSection section = config.getConfigurationSection("scoreboard.game-board");
        if (!section.getBoolean("enabled")) return;
        SimpleDateFormat dateFormat = new SimpleDateFormat(language.getString(Message.SCOREBOARD_DATE_FORMAT));
        String youText = language.getString(Message.SCOREBOARD_YOU_TEXT);
        final IGame game = player.getGame();
        BoardCreator creator = new BoardCreator(player) {
            @Override
            public String placeholder(String text) {
                text = text.replaceAll("<kills>", String.valueOf(game.getKills(player))
                        .replaceAll("<mode>", game.getGameMode().name()));
                return text;
            }
        };

        creator.setTitle(section.getString("title"));
        int id = Bukkit.getScheduler().runTaskTimerAsynchronously(REggWars.getInstance(), () -> {
            if (game == null) return;
            if (game.getGameState() == GameState.WAITING) {
                for (int i = 0; i < language.getStringList(Message.SCOREBOARD_WAITING_LINES).size(); i++) {
                    creator.setLine(i, language.getStringList(Message.SCOREBOARD_WAITING_LINES).get(i));
                }
                return;
            }

            List<String> lines = language.getStringList(Message.SCOREBOARD_PLAYING_LINES);
            List<String> list = new ArrayList<>();
            for (String line : lines) {
                if (line.contains("<teams>")) {
                    for (IGameTeam team : game.getTeamsOrdered()) {
                        String name = team.getTeam().getDisplayName();
                        String you = "";
                        if (team.hasPlayer(player)) {
                            name = team.getTeam().getBold();
                            you = " " + youText;
                        }

                        int alive = team.getAlivePlayers().size();
                        String aliveString = "";
                        if (alive == 0 && team.isEggGone()) {
                            aliveString = language.getString(Message.SCOREBOARD_DEAD_TEXT);
                        } else if (alive > 0 && team.isEggGone()) {
                            aliveString = "&a" + alive;
                        } else if (!team.isEggGone()) {
                            aliveString = language.getString(Message.SCOREBOARD_ALIVE_TEXT);
                        }
                        list.add(name + "&r: " + aliveString + you);
                    }
                }
                if (line.contains("<teams>")) continue;
                list.add(line);
                creator.setLines(list);
            }
        }, 0L, 20L).getTaskId();
        data.put(player.getUniqueId(), id);
        boards.put(player.getUniqueId(), creator);
    }


    public void removeScoreboard(IGamePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!data.containsKey(uuid)) return;
        player.getPlayer().setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
        Bukkit.getServer().getScheduler().cancelTask(data.get(uuid));
        data.remove(uuid);
        boards.remove(player.getUniqueId());
    }

    public void shutdown() {
        for (Integer integer : data.values()) {
            Bukkit.getScheduler().cancelTask(integer);
        }
        data.clear();
        boards.clear();
    }

    public Map<UUID, Integer> getData() {
        return data;
    }

    public Map<UUID, BoardCreator> getBoards() {
        return boards;
    }
}
