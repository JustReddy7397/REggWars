package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.commands.Command;
import org.bukkit.entity.Player;

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

}
