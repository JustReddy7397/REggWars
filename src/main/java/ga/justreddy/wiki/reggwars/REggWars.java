package ga.justreddy.wiki.reggwars;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.exceptions.DependencyNotInstalledException;
import ga.justreddy.wiki.reggwars.model.game.map.FlatAdapter;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeAdapter;
import ga.justreddy.wiki.reggwars.nms.Nms;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class REggWars extends JavaPlugin {

    @Getter
    private static REggWars instance;

    private boolean isSlimeEnabled;
    private SlimePlugin slime;
    private SlimeLoader loader;

    // ResetAdapter
    private ResetAdapter resetAdapter;

    private Nms nms;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;


        if (isSlimeEnabled) {
            slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
            if (slime == null) {
                Bukkit.getPluginManager().disablePlugin(this);
                throw new DependencyNotInstalledException("SlimeWorldManager");
            }
            loader = slime.getLoader("file");
            resetAdapter = new SlimeAdapter();
        } else {
            resetAdapter = new FlatAdapter();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
