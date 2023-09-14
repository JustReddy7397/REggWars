package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class MenuManager {

    private static MenuManager manager;

    public static MenuManager getManager() {
        return manager == null ? manager = new MenuManager() : manager;
    }

    private final File folder;
    @Getter
    private final Map<String, InventoryMenu> menus;
    @Getter
    private final Map<IGamePlayer, InventoryMenu> openMenus;

    private MenuManager() {
        this.folder = new File(REggWars.getInstance().getDataFolder(), "menus");
        if (!folder.exists()) folder.mkdirs();
        this.menus = new HashMap<>();
        this.openMenus = new HashMap<>();
    }

    public void start() {
        File[] menus = this.folder.listFiles();
        if (menus == null) return;
        for (File menu : menus) {
            if(!menu.getName().endsWith(".yml")) continue;
            String name = menu.getName().replace(".yml", "");
            if(name.isEmpty()) continue;
            if(this.menus.containsKey(name)) continue;
            register(name, YamlConfiguration.loadConfiguration(menu));
        }
    }

    public void register(String name, FileConfiguration configuration) {
/*        String identifier = configuration.getString("identifier");
        if(name.contains("cosmetics")) {
            .put(identifier, new CosmeticsMenu(configuration));
        }*/
    }

    public void reload() {
        shutdown();
        start();
    }

    public InventoryMenu getByName(String name) {
        return menus.getOrDefault(name, null);
    }

    public void shutdown() {
        openMenus.clear();
        menus.clear();
    }


}
