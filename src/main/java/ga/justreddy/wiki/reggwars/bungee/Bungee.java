package ga.justreddy.wiki.reggwars.bungee;

import ga.justreddy.wiki.reggwars.storage.MYSQLStorage;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author JustReddy
 */
public class Bungee extends Plugin {

    @Getter private static Bungee instance;
    @Getter private Storage storage;

    @Override
    public void onEnable() {
        instance = this;
        storage = new MYSQLStorage(); // TODO
    }

    @Override
    public void onDisable() {
    }
}
