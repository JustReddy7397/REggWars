package ga.justreddy.wiki.reggwars.model.game.items;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.ClickAction;
import ga.justreddy.wiki.reggwars.api.model.game.shop.item.CustomShopItem;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author JustReddy
 */
public class UpgradableAxeItem extends CustomShopItem {
    /**
     * Constructs a new CustomShopItem with the given id and starter item.
     *
     * @param id          the unique identifier for this item
     * @param starterItem the initial item stack for this item
     */
    public UpgradableAxeItem() {
        super("upgradable_axe",
                new ItemBuilder(XMaterial.WOODEN_AXE.parseItem())
                        .withEnchantment(Enchantment.DIG_SPEED, 2)
                        .setUnbreakable()
                        .build()
        );
        addUpgrade(new ItemBuilder(XMaterial.STONE_AXE.parseItem())
                .withEnchantment(Enchantment.DIG_SPEED, 2)
                .setUnbreakable()
                .build());
        addUpgrade(new ItemBuilder(XMaterial.IRON_AXE.parseItem())
                .withEnchantment(Enchantment.DIG_SPEED, 2)
                .setUnbreakable()
                .build());
        addUpgrade(new ItemBuilder(XMaterial.DIAMOND_AXE.parseItem())
                .withEnchantment(Enchantment.DIG_SPEED, 2)
                .setUnbreakable()
                .build());
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public void onDeath(IGamePlayer player) {}

    @Override
    public void onRespawn(IGamePlayer player) {
        ItemStack previous = previousUpgrade(player);
        Player bukkitPlayer = player.getPlayer();
        bukkitPlayer.getInventory().addItem(previous);

    }

    @Override
    public void onClick(IGamePlayer player, ClickAction action) {}
}
