package ga.justreddy.wiki.reggwars.commands;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;

/**
 * @author JustReddy
 */
public class AdminCommand extends Command{

    public AdminCommand(REggWars plugin) {
        super(plugin);
        setName("admin");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {
        // TODO add reload command
    }
}
