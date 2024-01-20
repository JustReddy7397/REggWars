package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.model.gui.editable.guis.CosmeticMenu;
import ga.justreddy.wiki.reggwars.model.gui.editable.guis.OtherMenu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
        String identifier = configuration.getString("identifier");
        if(identifier == null) return;
        if(identifier.isEmpty()) return;
        if (identifier.equals("cosmetic")) {
            this.menus.put(name.split("_")[0], new CosmeticMenu(configuration));
        } else if (identifier.equals("other")) {
            this.menus.put(name.split("_")[0], new OtherMenu(configuration));
        }
    }

    public void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
            if (gamePlayer == null) continue;
            InventoryMenu menu = openMenus.get(gamePlayer);
            if (menu == null) continue;
            menu.onClose();
            if (player.getOpenInventory().getTopInventory() == menu.getInventory()) {
                player.closeInventory();
            }
            openMenus.remove(gamePlayer);
        }
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
