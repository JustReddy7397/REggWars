package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.manager.HookManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author andrei1058
 */
public class VillagerShop extends EntityVillager {

    private Hologram hologram;

    public VillagerShop(Location loc) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(this.goalSelector, new UnsafeList<>());
            bField.set(this.targetSelector, new UnsafeList<>());
            cField.set(this.goalSelector, new UnsafeList<>());
            cField.set(this.targetSelector, new UnsafeList<>());
        } catch (Exception ignored) {
        }
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        persistent = true;
        if (HookManager.getManager().hasHook("DecentHolograms")) {
            this.hologram = REggWars.getInstance().getNms().spawnHologram(
                    "shop_" + UUID.randomUUID().toString().substring(0, 8),
                    loc.clone().add(0, 2.5, 0),
                    null,
                    Arrays.asList("&a&lShop", "&7Right click to open")
            );
            for (Player player : world.getWorld().getPlayers()) {
                if (hologram.isShowState(player)) continue;
                hologram.show(player, 0);
            }
        }
    }

    public static Villager spawn(Location location) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        VillagerShop shop = new VillagerShop(location);
        shop.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        EntityTypes.spawnEntity(shop, location);

        return (Villager) shop.getBukkitEntity();
    }

    public void move(double d0, double d1, double d2) {
    }

    public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public void g(double d0, double d1, double d2) {
    }

    public void makeSound(String s, float f, float f1) {

    }

    @Override
    public void die() {
        this.hologram.destroy();
        this.hologram = null;
        super.die();
    }

    @Override
    public void die(DamageSource damagesource) {
        this.hologram.destroy();
        this.hologram = null;
        super.die(damagesource);
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
    }


}
