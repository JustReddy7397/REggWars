package ga.justreddy.wiki.reggwars.commands;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;

/**
 * @author JustReddy
 */
public class AdminCommand extends Command {

    public AdminCommand(REggWars plugin) {
        super(plugin);
        setName("admin");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {
        if (args.length < 2) {
            // TODO
            gamePlayer.sendLegacyMessage("&cUsage: /eggwars admin <subcommand>");
            return;
        }

        switch (args[1]) {
            case "reload":
                runReloadCommand(gamePlayer);
                break;
        }

    }

    private void runReloadCommand(IGamePlayer player) {

    }

}
