package ga.justreddy.wiki.reggwars.model.gui.editable.guis;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.manager.MenuManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

/**
 * @author JustReddy
 */
public class OtherMenu extends InventoryMenu {

    private final FileConfiguration configuration;
    private final String name;
    private final String identifier;
    private final int rows;
    private final int refreshRate;
    private final boolean refreshEnabled;

    public OtherMenu(FileConfiguration configuration) {
        this.configuration = configuration;
        this.name = ChatUtil.format(configuration.getString("options.name"));
        this.identifier = configuration.getString("options.identifier");
        this.rows = configuration.getInt("options.rows");
        this.refreshRate = configuration.getInt("settings.refresh.rate");
        this.refreshEnabled = configuration.getBoolean("settings.refresh.enabled");
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int refreshRate() {
        return refreshRate;
    }

    @Override
    public boolean isRefreshEnabled() {
        return refreshEnabled;
    }

    @Override
    public String placeholders(String text, IGamePlayer gamePlayer) {
        if (REggWars.getInstance().isPlaceholderAPIEnabled()) {
            text = PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), text);
        }
        return text;
    }

    @Override
    public void setMenuItems(IGamePlayer gamePlayer) {
        ConfigurationSection section = this.configuration.getConfigurationSection("items");
        for (String path : section.getKeys(false)) {
            ConfigurationSection button = section.getConfigurationSection(path);
            ItemBuilder itemBuilder = ItemBuilder.getItemStack(button);
            List<String> lore = button.getStringList("lore");
            lore.replaceAll((s) -> placeholders(s, gamePlayer));
            itemBuilder.withLore(lore);
            inventory.setItem(button.getInt("slot"), itemBuilder.build());
        }
    }

    @Override
    public void inventoryClick(InventoryClickEvent e) {
        IGamePlayer gamePlayer =
                PlayerManager.getManager().getGamePlayer(e.getWhoClicked().getUniqueId());
        Player player = gamePlayer.getPlayer();
        ConfigurationSection section = this.configuration.getConfigurationSection("items");
        for (String path : section.getKeys(false)) {
            ConfigurationSection button = section.getConfigurationSection(path);
            if (e.getSlot() == button.getInt("slot")) {
                for (String action : button.getStringList("actions")) {
                    String[] actions = action.split(";");
                    switch (actions[0]) {
                        case "inventory":
                            MenuManager.getManager().getByName(actions[1]).open(gamePlayer);
                            break;
                        case "playewr":
                            player.performCommand(actions[1]);
                            break;
                        case "close":
                            player.closeInventory();
                            break;
                        case "message":
                            gamePlayer.sendLegacyMessage(actions[1]);
                            break;
                        case "console":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), actions[1]);
                            break;
                        case "sound ":
                            gamePlayer.sendSound(actions[1]);
                            break;
                    }
                }
                break;
            }
        }
    }
}
