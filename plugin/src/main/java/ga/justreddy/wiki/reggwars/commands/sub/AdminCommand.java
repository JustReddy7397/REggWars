package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.manager.LeaderboardManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author JustReddy
 */
public class AdminCommand extends Command {

    public AdminCommand(REggWars plugin) {
        super(plugin);
        setName("admin");
        setPermission("eggwars.command.admin");
        setSyntax("/eggwars admin <subcommand>");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {
        Player bukkitPlayer = gamePlayer.getPlayer();
        if (bukkitPlayer == null) return;


        if (args.length < 2) {
            gamePlayer.sendMessage(
                    Message.MESSAGES_SERVER_INVALID_ARGUMENTS,
                    new Replaceable("<usage>", getSyntax())
            );
            return;
        }

        switch (args[1]) {
            case "reload":
                runReloadCommand(gamePlayer);
                break;
            case "setspawn":
                runSetSpawnCommand(gamePlayer);
                break;
            case "leaderboard":
                // TODO
                break;
        }

    }

    private void runReloadCommand(IGamePlayer player) {
        REggWars.getInstance().reload();
        player.sendMessage(
                Message.MESSAGES_SERVER_RELOADED
        );
    }

    private void runSetSpawnCommand(IGamePlayer player) {
        REggWars.getInstance().setSpawnLocation(player.getPlayer().getLocation());
        player.sendMessage(Message.MESSAGES_SERVER_SPAWN_SET);
    }

    private void runLeaderboardCommand(IGamePlayer player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(
                    Message.MESSAGES_SERVER_INVALID_ARGUMENTS,
                    new Replaceable("<usage>", "/eggwars admin leaderboard <add/remove> <id>")
            );
            return;
        }

        if (args[2].equalsIgnoreCase("add")) {

            String leaderboardId = args[3];

            if (!LeaderboardManager.getInstance().exists(leaderboardId)) {
                player.sendMessage(
                        Message.MESSAGES_SERVER_LEADERBOARD_DOES_NOT_EXIST,
                        new Replaceable("<id>", leaderboardId)
                );
                Set<String> leaderboardIds = LeaderboardManager.getInstance().getLeaderboards().keySet();
                List<String> list = new ArrayList<>();
                leaderboardIds.forEach(id -> list.add("&7- &a" + id));
                player.sendMessage(
                        Message.MESSAGES_SERVER_LEADERBOARD_LIST,
                        new Replaceable("<list>", String.join("\n", list))
                );
                return;
            }

            // TODO write to file and equip leaderboard to all players in lobby :)

        } else if (args[2].equalsIgnoreCase("remove")) {
            // TODO
        }



    }

}
