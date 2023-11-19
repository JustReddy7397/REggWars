package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.hologram.IArmorStand;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Collection;

/**
 * @author JustReddy
 */
public class CraftHologram extends CraftArmorStand implements IArmorStand {

    public CraftHologram(CraftServer server, EntityArmorStand entity) {
        super(server, entity);
    }

    @Override
    public int getId() {
        return entity.getId();
    }

    @Override
    public void setName(IGamePlayer player, String text) {
        ((EntityHologram) entity).setName(player, text);
    }

    @Override
    public void killEntity(IGamePlayer player) {
        ((EntityHologram) entity).killEntity(player);
    }

    @Override
    public IHologramLine getLine() {
        return ((EntityHologram) entity).getLine();
    }

    @Override
    public ArmorStand getEntity() {
        return this;
    }

    @Override
    public void setLocation(double x, double y, double z) {
        ((EntityHologram) entity).setLocation(x, y, z);
    }

    public void remove() {}

    public void setArms(boolean arms) {}

    public void setBasePlate(boolean basePlate) {}

    public void setBodyPose(EulerAngle pose) {}

    public void setGravity(boolean gravity) {}

    public void setHeadPose(EulerAngle pose) {}

    public void setLeftArmPose(EulerAngle pose) {}

    public void setLeftLegPose(EulerAngle pose) {}

    public void setRightArmPose(EulerAngle pose) {}

    public void setRightLegPose(EulerAngle pose) {}

    public void setSmall(boolean small) {}

    public void setVisible(boolean visible) {}

    public boolean addPotionEffect(PotionEffect effect) {
        return false;
    }

    public boolean addPotionEffect(PotionEffect effect, boolean param) {
        return false;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        return false;
    }

    public void setRemoveWhenFarAway(boolean remove) {}

    public void setVelocity(Vector vel) {}

    public boolean teleport(Location loc) {
        return false;
    }

    public boolean teleport(Entity entity) {
        return false;
    }

    public boolean teleport(Location loc, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    public void setFireTicks(int ticks) {}

    public boolean setPassenger(Entity entity) {
        return false;
    }

    public boolean eject() {
        return false;
    }

    public boolean leaveVehicle() {
        return false;
    }

    public void playEffect(EntityEffect effect) {}

    public void setCustomName(String name) {}

    public void setCustomNameVisible(boolean flag) {}
}
