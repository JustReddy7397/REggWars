package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.hologram.IArmorStand;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import ga.justreddy.wiki.reggwars.nms.v1_8_R3.box.NullBoundingBox;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.Field;

/**
 * @author JustReddy
 */
public class HologramStand extends EntityArmorStand implements IArmorStand {

    public HologramStand(Location toSpawn) {
        super(((CraftWorld) toSpawn.getWorld()).getHandle());
        setArms(false);
        setBasePlate(true);
        setInvisible(true);
        setGravity(false);
        setSmall(true);

        try {
            Field field = net.minecraft.server.v1_8_R3.EntityArmorStand.class.getDeclaredField("bi");
            field.setAccessible(true);
            field.set(this, 2147483647);
        } catch (Exception e) {
            e.printStackTrace();
        }
        a(new NullBoundingBox());
    }

    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    public void setCustomName(String customName) {}

    public void setCustomNameVisible(boolean visible) {}

    public void t_() {
        this.ticksLived = 0;
        super.t_();
    }

    public void makeSound(String sound, float f1, float f2) {}

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setName(IGamePlayer player, String name) {
        if (name != null && name.length() > 300) {
            name = name.substring(0, 300);
        }
        if (name == null) name = "";
        super.setCustomName(PlaceholderAPI.setPlaceholders(player.getPlayer(), name));
        super.setCustomNameVisible(!name.isEmpty());
    }

    @Override
    public void killEntity() {
        super.die();
    }

    @Override
    public IHologramLine getLine() {
        return null;
    }

    @Override
    public ArmorStand getEntity() {
        return (ArmorStand) getBukkitEntity();
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftStand(this);
        }

        return super.getBukkitEntity();
    }

    @Override
    public void setLocation(double x, double y, double z) {
        super.setPosition(x, y, z);

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(getId(), MathHelper.floor(this.locX * 32.0D), MathHelper.floor(this.locY * 32.0D),
                MathHelper.floor(this.locZ * 32.0D), (byte) (int) (this.yaw * 256.0F / 360.0F), (byte) (int) (this.pitch * 256.0F / 360.0F), this.onGround);

        for (EntityHuman obj : world.players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = square(nmsPlayer.locX - this.locX) + square(nmsPlayer.locZ - this.locZ);
                if (distanceSquared < 8192.0 && nmsPlayer.playerConnection != null) {
                    nmsPlayer.playerConnection.sendPacket(teleportPacket);
                }
            }
        }
    }

    private static double square(double num) {
        return num * num;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    static class CraftStand extends CraftArmorStand implements IArmorStand {

        public CraftStand(HologramStand entity) {
            super(entity.world.getServer(), entity);
        }

        @Override
        public int getId() {
            return ((HologramStand) entity).getId();
        }

        @Override
        public void setName(IGamePlayer player, String name) {
            ((HologramStand) entity).setName(player, name);
        }

        @Override
        public void setHeadPose(EulerAngle pose) {
            ((HologramStand) entity).setHeadPose(new Vector3f((float)pose.getX(), (float)pose.getY(), (float)pose.getZ()));
        }

        @Override
        public void setLeftArmPose(EulerAngle pose) {
            ((HologramStand) entity).setLeftArmPose(new Vector3f((float)pose.getX(), (float)pose.getY(), (float)pose.getZ()));
        }

        @Override
        public void setLeftLegPose(EulerAngle pose) {
            ((HologramStand) entity).setLeftLegPose(new Vector3f((float)pose.getX(), (float)pose.getY(), (float)pose.getZ()));
        }

        @Override
        public void setRightLegPose(EulerAngle pose) {
            ((HologramStand) entity).setRightLegPose(new Vector3f((float)pose.getX(), (float)pose.getY(), (float)pose.getZ()));
        }

        @Override
        public void killEntity() {
            ((HologramStand) entity).killEntity();
        }

        @Override
        public IHologramLine getLine() {
            return ((HologramStand) entity).getLine();
        }

        @Override
        public ArmorStand getEntity() {
            return this;
        }

        @Override
        public void setLocation(double x, double y, double z) {
            ((HologramStand) entity).setLocation(x, y, z);
        }
    }
}
