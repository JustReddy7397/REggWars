package ga.justreddy.wiki.reggwars.utils;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import ga.justreddy.wiki.reggwars.REggWars;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class ItemBuilder {

    private final ItemStack stack;
    @Getter
    private boolean hasUsername;
    @Getter
    private boolean hasTexture;
    @Getter
    private String texture;


    public ItemBuilder(ItemStack itemStack) {
        stack = itemStack;
    }

    public static ItemBuilder getItemStack(ConfigurationSection section, Player player) {
        ItemStack item = XMaterial.matchXMaterial(section.getString("material").toUpperCase()).get().parseItem();
        ItemBuilder builder = new ItemBuilder(item);
        if (section.contains("amount")) {
            builder.withAmount(section.getInt("amount"));
        }

        if (section.contains("username") && player != null) {
            builder.setSkullOwner(section.getString("username").replace("<player>", player.getName()));
        }

        if (section.contains("texture")) {
            builder.setTexture(section.getString("texture"));

        }

        if (section.contains("displayname")) {
            if (player != null) builder.withName(section.getString("displayname"));
            else builder.withName(section.getString("displayname"));
        }

        if (section.contains("lore")) {
            if (player != null) builder.withLore(section.getStringList("lore"));
            else builder.withLore(section.getStringList("lore"));
        }

        if (section.contains("glow") && section.getBoolean("glow")) {
            builder.withGlow();
        }

        if (section.contains("item_flags")) {
            List<ItemFlag> flags = new ArrayList<>();
            section.getStringList("item_flags").forEach(text -> {
                try {
                    ItemFlag flag = ItemFlag.valueOf(text);
                    flags.add(flag);
                } catch (IllegalArgumentException ignored) {
                }
            });
            builder.withFlags(flags.toArray(new ItemFlag[0]));
        }

        return builder;
    }

    public static ItemBuilder getItemStack(ConfigurationSection section) {
        return getItemStack(section, null);
    }


    public ItemBuilder withAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... flags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flags);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withName(String name) {
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatUtil.format(name));
        stack.setItemMeta(meta);
        return this;
    }

    /*public ItemBuilder withName(String name, Player player) {
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatUtil.format(PlaceholderAPI.setPlaceholders(player, name)));
        stack.setItemMeta(meta);
        return this;
    }*/

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) stack.getItemMeta();
            im.setOwner(owner);
            stack.setItemMeta(im);
            hasUsername = true;
        } catch (ClassCastException expected) {
        }
        return this;
    }

/*    public ItemBuilder withLore(List<String> lore, Player player) {
        final ItemMeta meta = stack.getItemMeta();
        List<String> coloredLore = new ArrayList<String>();
        for (String s : lore) {
            if (HookManager.getManager().hasHook("PlaceholderAPI")) {
                s = PlaceholderAPI.setPlaceholders(player, s);
            }
            coloredLore.add(ChatUtil.format(s));
        }
        meta.setLore(coloredLore);
        stack.setItemMeta(meta);
        return this;
    }*/

    public ItemBuilder withLore(List<String> lore) {
        final ItemMeta meta = stack.getItemMeta();
        List<String> coloredLore = new ArrayList<>();
        for (String s : lore) {
            coloredLore.add(ChatUtil.format(s));
        }
        meta.setLore(coloredLore);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String line) {
        final ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(ChatUtil.format(line));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return this;
    }


    @SuppressWarnings("deprecation")
    public ItemBuilder withDurability(int durability) {
        stack.setDurability((short) durability);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder withData(int data) {
        stack.setDurability((short) data);
        return this;
    }

    @SneakyThrows
    public ItemBuilder setTexture(String texture) {
        if (texture == null) return this;
        ItemMeta ITEM_META = stack.getItemMeta();
        if (stack.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) {
            return this;
        }

        if (REggWars.getInstance().getNms().isLegacyVersion()) {
            stack.setDurability((short) 3);
        }
        SkullMeta skullMeta = (SkullMeta) ITEM_META;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField = skullMeta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        profileField.set(skullMeta, profile);
        hasTexture = true;
        this.texture = texture;
        return this;
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, final int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder withEnchantment(Enchantment enchantment) {
        stack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder withGlow() {
        final ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        stack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        return this;
    }

    public ItemBuilder withType(Material material) {
        stack.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        final ItemMeta meta = stack.getItemMeta();
        meta.setLore(new ArrayList<String>());
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (Enchantment enchantment : stack.getEnchantments().keySet()) {
            stack.removeEnchantment(enchantment);
        }
        return this;
    }

    public ItemBuilder withColor(Color color) {
        Material type = stack.getType();
        if (type == Material.LEATHER_BOOTS || type == Material.LEATHER_CHESTPLATE || type == Material.LEATHER_HELMET || type == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            meta.setColor(color);
            stack.setItemMeta(meta);
            return this;
        } else if (type == Material.WOOL) {
            MaterialData data = stack.getData();
            Wool wool = (Wool) data;
            wool.setColor(DyeColor.getByColor(color));
            stack.setData(wool);
            return this;
        } else if (type == Material.STAINED_GLASS_PANE || type == Material.STAINED_GLASS) {
            MaterialData data = stack.getData();
            Dye dye = (Dye) data;
            dye.setColor(DyeColor.getByColor(color));
            stack.setData(dye);
            return this;
        } else {
            throw new IllegalArgumentException("withColor is only applicable for leather armor, wool or glass (panes)!!");

        }
    }

    public ItemBuilder withPotion(PotionType potionType, int level, boolean splash, boolean hasExtendedDuration) {
        Material type = stack.getType();
        if (type != Material.POTION) {
            throw new IllegalArgumentException("withPotion is only applicable for potions!");
        }
        Potion potion = new Potion(potionType);
        potion.setLevel(level);
        potion.setSplash(splash);
        if (hasExtendedDuration) {
            potion.setHasExtendedDuration(true);
        }
        potion.apply(stack);
        return this;
    }

    public ItemBuilder withPotion(PotionType potion, PotionEffect effect) {
        Material type = stack.getType();
        if (type != Material.POTION) {
            throw new IllegalArgumentException("withPotion is only applicable for potions!");
        }
        Potion potion1 = new Potion(potion);
        potion1.setSplash(true);
        potion1.apply(stack);
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.addCustomEffect(effect, true);
        if (!potion.getEffectType().equals(effect.getType())) {
            meta.removeCustomEffect(potion.getEffectType());
        }
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withPotion(PotionType potion, boolean splash, PotionEffect effect) {
        Material type = stack.getType();
        if (type != Material.POTION) {
            throw new IllegalArgumentException("withPotion is only applicable for potions!");
        }
        Potion potion1 = new Potion(potion);
        potion1.setSplash(splash);
        potion1.apply(stack);
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.addCustomEffect(effect, true);
        if (!potion.getEffectType().equals(effect.getType())) {
            meta.removeCustomEffect(potion.getEffectType());
        }
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withBookEnchantments(Enchantment enchantment, int level) {
        Material type = stack.getType();
        if (type != Material.ENCHANTED_BOOK) {
            throw new IllegalArgumentException("withBookEnchantment is only applicable for enchantment books!");
        }
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
        meta.addStoredEnchant(enchantment, level, false);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder withBase64(String texture) {
        Material type = stack.getType();
        if (type != XMaterial.PLAYER_HEAD.parseMaterial()) {
            throw new IllegalArgumentException("withBase64 is only applicable for player heads!");
        }

        final SkullMeta meta = (SkullMeta) stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", texture));
        meta.setDisplayName("");
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (final IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return stack;
    }

    public ItemMeta getItemMeta() {
        return stack.getItemMeta();
    }

    @Override
    public ItemBuilder clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Material getType() {
        return stack.getType();
    }
}
