package ga.justreddy.wiki.reggwars.commands;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public abstract class Command {

    protected final REggWars plugin;

    public Command(REggWars plugin) {
        this.plugin = plugin;
    }

    String name;
    String description;
    String syntax;
    String permission;
    List<String> aliases;

    public abstract void onCommand(IGamePlayer gamePlayer, String[] args);

}
