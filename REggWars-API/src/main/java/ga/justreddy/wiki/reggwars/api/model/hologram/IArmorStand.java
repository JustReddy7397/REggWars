package ga.justreddy.wiki.reggwars.api.model.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.entity.ArmorStand;

/**
 * @author JustReddy
 */
public interface IArmorStand {

    int getId();

    void setName(IGamePlayer player, String name);

    void setLocation(double x, double y, double z);

    boolean isDead();

    void killEntity();

    ArmorStand getEntity();

    IHologramLine getLine();

}
