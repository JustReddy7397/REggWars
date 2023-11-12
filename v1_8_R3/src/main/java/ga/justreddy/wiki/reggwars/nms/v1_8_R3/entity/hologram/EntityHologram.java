package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.hologram.IArmorStand;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologram;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import ga.justreddy.wiki.reggwars.nms.v1_8_R3.box.NullBoundingBox;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * @author JustReddy
 */
public class EntityHologram extends EntityArmorStand implements IArmorStand {

    private final IHologramLine line;


    public EntityHologram(World world, IHologramLine line) {
        super(world);
        setInvisible(true);
        setSmall(true);
        setArms(false);
        setGravity(true);
        setBasePlate(true);
        this.line = line;
        try {
            Field field = net.minecraft.server.v1_8_R3.EntityArmorStand.class.getDeclaredField("bi");
            field.setAccessible(true);
            field.set(this, 2147483647);
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
        a(new NullBoundingBox());
    }

    public static IArmorStand spawn(Location location, IGamePlayer player, String name, IHologramLine line) {
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        IArmorStand armor = line == null ? new HologramStand(location)
                : new EntityHologram(((CraftWorld) location.getWorld()).getHandle(), line);
        armor.setLocation(location.getX(), location.getY(), location.getZ());
        armor.setName(player, name);
        if (armor instanceof HologramStand) {
            HologramStand stand = (HologramStand) armor;
            stand.yaw = location.getYaw();
            stand.pitch = location.getPitch();
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        } else if (armor instanceof EntityHologram) {
            EntityHologram stand = (EntityHologram) armor;
            stand.yaw = location.getYaw();
            stand.pitch = location.getPitch();
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        return armor;
    }

    public void a(NBTTagCompound nbttagcompound) {
    }

    public void b(NBTTagCompound nbttagcompound) {
    }

    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void e(NBTTagCompound nbttagcompound) {
    }

    public void f(NBTTagCompound nbttagcompound) {
    }

    public boolean isInvulnerable(DamageSource source) {
        return true;
    }

    public void setCustomName(String customName) {
    }

    public void setCustomNameVisible(boolean visible) {
    }

    public boolean a(EntityHuman human, Vec3D vec3d) {
        return true;
    }

    public void t_() {
        this.ticksLived = 0;
        if (dead) {
            super.t_();
        }
    }

    public void makeSound(String sound, float f1, float f2) {
    }

    public CraftEntity getBukkitEntity() {
        if (this.bukkitEntity == null) {
            this.bukkitEntity = new CraftHologram(world.getServer(), this);
        }
        return this.bukkitEntity;
    }

    public void die() {
        super.die();
    }

    @Override
    public void setLocation(double x, double y, double z) {
        super.setPosition(x, y, z);

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(getId(),
                MathHelper.floor(this.locX * 32.0D), MathHelper.floor(this.locY * 32.0D),
                MathHelper.floor(this.locZ * 32.0D), (byte) (int) (this.yaw * 256.0F / 360.0F),
                (byte) (int) (this.pitch * 256.0F / 360.0F), this.onGround);

        for (EntityHuman obj : world.players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared =
                        square(nmsPlayer.locX - this.locX) + square(nmsPlayer.locZ - this.locZ);
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

    @Override
    public void killEntity() {
        die();
    }

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
    public ArmorStand getEntity() {
        return (ArmorStand) getBukkitEntity();
    }

    @Override
    public IHologramLine getLine() {
        return line;
    }
}
