package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public class AdminCommand extends Command {

    public AdminCommand(REggWars plugin) {
        super(plugin);
        setName("admin");
        setPermission("eggwars.command.admin");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {
        Player bukkitPlayer = gamePlayer.getPlayer();
        if (bukkitPlayer == null) return;


        if (args.length < 2) {
            // TODO
            gamePlayer.sendLegacyMessage("&cUsage: /eggwars admin <subcommand>");
            return;
        }

        switch (args[1]) {
            case "reload":
                runReloadCommand(gamePlayer);
                break;
            case "setspawn":
                runSetSpawnCommand(gamePlayer);
                break;
        }

    }

    private void runReloadCommand(IGamePlayer player) {
        REggWars.getInstance().reload();
        player.sendLegacyMessage("&aSuccessfully reloaded the plugin!");
    }

    private void runSetSpawnCommand(IGamePlayer player) {
        REggWars.getInstance().setSpawnLocation(player.getPlayer().getLocation());
        player.sendLegacyMessage("&aSuccessfully set the spawn location!");
    }

}
