package ga.justreddy.wiki.reggwars.model.game.shop;

import com.cryptomorin.xseries.XMaterial;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopPrice;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Optional;

/**
 * @author JustReddy
 */
public class ShopItem implements IShopItem {

    private final ItemStack item;
    private final IShopPrice price;
    private final int amount;
    private final int slot;
    private final boolean shouldColor;
    private final boolean isBuyable;
    private final List<String> actions;

    public ShopItem(ConfigurationSection section) {
        Optional<XMaterial> item = XMaterial.matchXMaterial(section.getString("item.material"));
        if (!item.isPresent()) throw new NullPointerException("The material " + section.getString("item.material") + " is not a valid material!");
        ItemBuilder builder = new ItemBuilder(item.get().parseItem());
        if (section.contains("item.name")) builder.withName(section.getString("item.name"));
        if (section.contains("item.lore")) builder.withLore(section.getStringList("item.lore"));
        this.item = builder.build();
        Optional<XMaterial> priceMaterial = XMaterial.matchXMaterial(section.getString("price.material"));
        if (!priceMaterial.isPresent()) throw new NullPointerException("The material " + section.getString("price.material") + " is not a valid material!");
        int price = section.getInt("price.amount");
        this.price = new ShopPrice(priceMaterial.get().parseMaterial(), price);
        this.amount = section.getInt("amount");
        this.slot = section.getInt("slot");
        this.shouldColor = section.getBoolean("color");
        this.isBuyable = section.getBoolean("buyable");
        this.actions = section.getStringList("actions");
        this.item.setAmount(amount);
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public IShopPrice getPrice() {
        return price;
    }


    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public boolean shouldColor() {
        return shouldColor;
    }

    @Override
    public boolean isBuyable() {
        return isBuyable;
    }

    @Override
    public void give(IGamePlayer player) {
        if (!isBuyable) return;

        for (String action : actions) {
            String[] split = action.split(":");
            if (split[0].equalsIgnoreCase("category")) {
                IShopCategory category = ShopManager.getManager().getCategoryById(split[1]);
                if (category == null) {
                    REggWars.getInstance().getLogger().warning("The shop category " + split[1] + " does not exist!");
                    continue;
                }
                category.getGui().open(player);
                continue;
            }

            if (split[0].equalsIgnoreCase("give")) {
                // TODO but for now this
                if (!hasEnoughMoney(player)) {
                    player.sendLegacyMessage("&cYou don't have enough money!");
                    return;
                }

                final Player p = player.getPlayer();
                if (p == null) return;
                final PlayerInventory inventory = p.getInventory();
                if (shouldColor) {
                    // TODO
                }
                inventory.addItem(item);

            }

        }
        // TODO

    }

    private boolean hasEnoughMoney(IGamePlayer gamePlayer) {
        final Player player = gamePlayer.getPlayer();
        if (player == null) return false;
        final PlayerInventory inventory = player.getInventory();
        if (!inventory.contains(price.getMaterial())) return false;
        final ItemStack[] contents = inventory.getContents();
        for (ItemStack content : contents) {
            if (content == null) continue;
            if (content.getType() != price.getMaterial()) continue;
            if (content.getAmount() < price.getPrice()) continue;
            return true;
        }
        return false;
    }

}
