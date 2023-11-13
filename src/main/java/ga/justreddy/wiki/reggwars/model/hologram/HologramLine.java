package ga.justreddy.wiki.reggwars.model.hologram;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.hologram.IArmorStand;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologram;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.Location;

/**
 * @author JustReddy
 */
public class HologramLine implements IHologramLine {

    private Location location;
    private IArmorStand armor;
    private String line;
    private IHologram hologram;

    public HologramLine(IHologram hologram, Location location, String line) {
        this.line = ChatUtil.format(line);
        this.location = location;
        this.armor = null;
        this.hologram = hologram;
    }

    @Override
    public void spawn(IGamePlayer player) {
        if (armor == null) {
            this.armor = REggWars.getInstance().getNms().spawnArmorStand(
                    location, player, line, this);
        }
    }

    @Override
    public void despawn(IGamePlayer player) {
        if (armor != null) {
            this.armor.killEntity();
            this.armor = null;
        }
    }

    @Override
    public void setLocation(Location location) {
        if (armor != null) {
            this.armor.setLocation(location.getX(), location.getY(), location.getZ());
        }
    }

    @Override
    public void setLine(IGamePlayer player, String line) {
        if (this.line.equals(ChatUtil.format(line))) {
            this.line = ChatUtil.format(this.line + "&r");
            armor.setName(player, ChatUtil.format(line));
            return;
        }

        this.line = ChatUtil.format(line);
        if (armor == null) {
            if (hologram.isSpawned()) {
                this.spawn(player);
            }

            return;
        }

        this.armor.setName(player, ChatUtil.format(this.line));
        return;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public IArmorStand getStand() {
        return armor;
    }

    @Override
    public String getLine() {
        return line;
    }

    @Override
    public IHologram getHologram() {
        return hologram;
    }
}
