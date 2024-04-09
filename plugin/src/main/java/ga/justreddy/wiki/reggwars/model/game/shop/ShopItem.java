package ga.justreddy.wiki.reggwars.model.game.shop;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopCategory;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopGui;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopItem;
import ga.justreddy.wiki.reggwars.api.model.game.shop.IShopPrice;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.manager.ShopManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.gui.custom.guis.QuickBuyEditorGui;
import ga.justreddy.wiki.reggwars.utils.ItemBuilder;
import ga.justreddy.wiki.reggwars.utils.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class ShopItem implements IShopItem {

    private final String id;
    private ItemStack item;
    private IShopPrice price;
    private int amount = 0;
    private int[] slots;
    private final boolean shouldColor;
    private final boolean canQuickBuy;
    private boolean dummy;
    private final List<String> actions;

    public ShopItem(XMaterial material) {
        this.id = material.name() + UUID.randomUUID().toString().substring(0, 6);
        this.item = material.parseItem();
        this.dummy = true;
        this.slots = new int[]{0};
        this.shouldColor = false;
        this.actions = new ArrayList<>();
        this.canQuickBuy = false;
    }

    public ShopItem(ConfigurationSection section, String id) {
        this.canQuickBuy = false;
        this.id = id + UUID.randomUUID().toString().substring(0, 6);
        ItemBuilder builder;
        if (section.getString("material").equals("FILLER")) {
            builder = new ItemBuilder(ShopManager.getManager().getMaterial().parseItem());
        } else {
            Optional<XMaterial> item = XMaterial.matchXMaterial(section.getString("material"));
            if (!item.isPresent())
                throw new NullPointerException("The material " + section.getString("material") + " is not a valid material!");
            builder = new ItemBuilder(item.get().parseItem());
        }
        this.dummy = true;
        if (section.contains("name")) builder.withName(section.getString("name"));
        if (section.contains("lore")) builder.withLore(section.getStringList("lore"));
        this.item = builder.build();
        this.slots = new int[]{0};
        this.shouldColor = false;
        this.actions = new ArrayList<>();
    }

    public ShopItem(ConfigurationSection section) {
        this.id = section.getString("id");
        ItemBuilder builder;
        if (section.getString("item.material").equals("FILLER")) {
            builder = new ItemBuilder(ShopManager.getManager().getMaterial().parseItem());
            this.dummy = true;
        } else {
            Optional<XMaterial> item = XMaterial.matchXMaterial(section.getString("item.material"));
            if (!item.isPresent())
                throw new NullPointerException("The material " + section.getString("item.material") + " is not a valid material!");
            builder = new ItemBuilder(item.get().parseItem());
            this.dummy = section.getBoolean("dummy");
        }
        if (!dummy) {
            Optional<XMaterial> priceMaterial = XMaterial.matchXMaterial(section.getString("price.material"));
            if (!priceMaterial.isPresent())
                throw new NullPointerException("The material " + section.getString("price.material") + " is not a valid material!");
            int price = section.getInt("price.amount");
            this.price = new ShopPrice(priceMaterial.get().parseMaterial(), price);
            this.amount = section.getInt("amount");
        }

        if (section.contains("item.name")) builder.withName(section.getString("item.name"));
        if (price != null) {
            if (section.contains("item.lore")) builder.withLore(
                    section.getStringList("item.lore"),
                    new Replaceable("<price>", price.getPrice() + " " + price.getMaterial().name())
            );
        } else {
            if (section.contains("item.lore")) builder.withLore(
                    section.getStringList("item.lore")
            );
        }

        this.item = builder.build();
        this.canQuickBuy = section.getBoolean("quick-buy");


        if (section.contains("enchantments")) {
            for (String enchantment : section.getStringList("enchantments")) {
                String[] split = enchantment.split(":");
                builder.withEnchantment(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
            }
        }

        if (section.contains("slots")) {
            String[] slots = section.getString("slots").split(", ");
            this.slots = new int[slots.length];
            for (int i = 0; i < slots.length; i++) {
                this.slots[i] = Integer.parseInt(slots[i]);
            }
        } else if (section.contains("slot")) {
            this.slots = new int[]{section.getInt("slot")};
        }
        this.shouldColor = section.getBoolean("color");
        this.actions = section.getStringList("actions");
        if (amount != 0) this.item.setAmount(amount);
    }

    @Override
    public String getId() {
        return id;
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
        return slots[0];
    }

    @Override
    public int[] getSlots() {
        return slots;
    }

    @Override
    public boolean shouldColor() {
        return shouldColor;
    }

    @Override
    public boolean isDummy() {
        return dummy;
    }

    @Override
    public boolean canQuickBuy() {
        return canQuickBuy;
    }

    @Override
    public void give(IShopGui gui, IGamePlayer player, boolean shift) {

        if (shift && canQuickBuy) {
            // TODO but for now this
            new QuickBuyEditorGui(gui, this).open(player);
            return;
        }

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

            if (dummy) return;

            if (split[0].equalsIgnoreCase("give")) {
                // TODO but for now this
                if (!hasEnoughMoney(player)) {
                    player.sendLegacyMessage("&cYou don't have enough money!");
                    return;
                }

                final Player p = player.getPlayer();
                if (p == null) return;
                final PlayerInventory inventory = p.getInventory();
                ItemBuilder builder = new ItemBuilder(new ItemStack(item.getType()));
                builder.withAmount(item.getAmount());
                if (item.getItemMeta().hasEnchants()) {
                    builder.withEnchantments(item.getItemMeta().getEnchants());
                }
                if (shouldColor) {
                    IGameTeam team = player.getTeam();
                    Color color = team.getTeam().getColor();
                    XMaterial woolMaterialByColor = Util.getWoolMaterialByColor(color);
                    builder.withType(woolMaterialByColor.parseMaterial());
                }
                takeMoney(player);
                inventory.addItem(builder.build());
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

    private void takeMoney(IGamePlayer player) {
        final Player p = player.getPlayer();
        if (p == null) return;
        final PlayerInventory inventory = p.getInventory();
        final ItemStack[] contents = inventory.getContents();
        for (ItemStack content : contents) {
            if (content == null) continue;
            if (content.getType() != price.getMaterial()) continue;
            if (content.getAmount() < price.getPrice()) continue;
            if (content.getAmount() == price.getPrice()) {
                inventory.removeItem(content);
                p.updateInventory();
                return;
            }
            content.setAmount(content.getAmount() - price.getPrice());
            p.updateInventory();
            return;
        }
    }


}
