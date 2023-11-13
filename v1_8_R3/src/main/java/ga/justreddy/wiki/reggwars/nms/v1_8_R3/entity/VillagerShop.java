package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

/**
 * @author andrei1058
 */
public class VillagerShop extends EntityVillager {
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

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
    }


}
