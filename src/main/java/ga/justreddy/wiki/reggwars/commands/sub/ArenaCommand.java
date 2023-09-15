package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author JustReddy
 */
public class ArenaCommand extends Command {

    public ArenaCommand(REggWars plugin) {
        super(plugin);
        setName("game");
        setDescription("Game commands");
        setAliases(Collections.singletonList("arena"));
        setSyntax("fuck you");
        setPermission("eggwars.command.game");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {

    }
}
