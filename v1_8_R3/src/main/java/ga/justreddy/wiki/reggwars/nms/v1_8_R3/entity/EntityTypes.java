package ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity;

import ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram.EntityHologram;
import ga.justreddy.wiki.reggwars.nms.v1_8_R3.entity.hologram.HologramStand;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author JustReddy
 */
public enum EntityTypes {

    HOLOGRAM("hologram", 30, EntityHologram.class),
    HOLOGRAM_2("hologram2", 30, HologramStand.class),
    VILLAGER_SHOP("VillagerShop", 120, VillagerShop.class);

    EntityTypes(String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    private static void addToMaps(Class<?> clazz, String name, int id) {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        ((Map) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, id);
        //((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }

    public static Object getPrivateField(String fieldName, Class<net.minecraft.server.v1_8_R3.EntityTypes> clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

}
