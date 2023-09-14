package ga.justreddy.wiki.reggwars.model.gui.editable.guis;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerCosmetics;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;
import ga.justreddy.wiki.reggwars.model.gui.editable.InventoryMenu;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

/**
 * @author JustReddy
 */
public class CosmeticMenu extends InventoryMenu {

    private final FileConfiguration configuration;
    private final String name;
    private final String identifier;
    private final int rows;
    private final int refreshRate;
    private final boolean refreshEnabled;

    public CosmeticMenu(FileConfiguration configuration) {
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
        IPlayerCosmetics cosmetics = gamePlayer.getCosmetics();
        Player player = gamePlayer.getPlayer();
        if (REggWars.getInstance().isPlaceholderAPIEnabled()) {
            text = PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), text);
        }
        ILanguage language = gamePlayer.getSettings().getLanguage();

        for (VictoryDance dance : DanceManager.getManager().getDances().values()) {
            text = text.replaceAll("<ew-dances-rarity-" + dance.getId() + ">",
                    dance.getRarity().getDisplay());
            if (cosmetics.hasDance(dance.getId()) || player.hasPermission(dance.getPermission())) {
                text = text.replaceAll("<ew-dances-status-" + dance.getId() + ">",
                        language.getString(Message.ENUMS_COSMETICS_UNLOCKED));
            } else {
                text = text.replaceAll("<ew-dances-status-" + dance.getId() + ">",
                        language.getString(Message.ENUMS_COSMETICS_LOCKED));
            }
            if (cosmetics.getSelectedDance() == dance.getId()) {
                text = text.replaceAll("<ew-dances-selected-" + dance.getId() + ">",
                        language.getString(Message.ENUMS_COSMETICS_SELECTED));
            } else {
                if (!player.hasPermission(dance.getPermission()) || !cosmetics.hasDance(dance.getId())) {
                    text = text.replaceAll("<ew-dances-selected-" + dance.getId() + ">",
                            language.getString
                                    (Message.ENUMS_COSMETICS_CANT_SELECT)
                                    .replaceAll("<cost>",
                                            String.valueOf(dance.getCost())));
                } else {
                    text = text.replaceAll("<ew-dances-selected-" + dance.getId() + ">", language.getString(Message.ENUMS_COSMETICS_NOT_SELECTED));
                }
            }
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
        // TODO
    }


}
