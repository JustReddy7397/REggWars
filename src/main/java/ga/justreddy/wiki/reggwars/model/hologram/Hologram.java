package ga.justreddy.wiki.reggwars.model.hologram;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologram;
import ga.justreddy.wiki.reggwars.api.model.hologram.IHologramLine;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class Hologram implements IHologram {

    private boolean spawned;
    private int current = 0;
    private final Location location;
    private final Map<Integer, HologramLine> lines;

    public Hologram(Location location, List<String> lines) {
        this.location = location;
        this.lines = new HashMap<>();
        for (String line : lines) {
            this.lines.put(++current,
                    new HologramLine(this, location.clone().add(0, -0.33 * this.lines.size() + 1, 0), line));
        }
    }


    @Override
    public IHologram spawn() {
        if (spawned) return this;

        for (Player player : Bukkit.getOnlinePlayers()) {
            spawn(PlayerManager.getManager().getGamePlayer(player.getUniqueId()));
        }

        return this;
    }

    @Override
    public IHologram spawn(World world) {
        if (spawned) return this;

        for (Player player : world.getPlayers()) {
            spawn(PlayerManager.getManager().getGamePlayer(player.getUniqueId()));
        }

        return this;
    }

    @Override
    public IHologram spawn(IGamePlayer player) {
        if (spawned) {
            return this;
        }

        lines.values().forEach(hologramLine -> hologramLine.spawn(player));
        this.spawned = true;
        return this;
    }

    @Override
    public IHologram despawn() {
        if (!spawned) return this;

        for (Player player : Bukkit.getOnlinePlayers()) {
            despawn(PlayerManager.getManager().getGamePlayer(player.getUniqueId()));
        }

        return this;
    }

    @Override
    public IHologram despawn(World world) {
        if (!spawned) return this;

        for (Player player : world.getPlayers()) {
            despawn(PlayerManager.getManager().getGamePlayer(player.getUniqueId()));
        }

        return this;
    }

    @Override
    public IHologram despawn(IGamePlayer player) {
        if (!spawned) {
            return this;
        }

        lines.values().forEach(hologramLine -> hologramLine.despawn(player));
        this.spawned = false;
        return this;
    }

    @Override
    public IHologram withLine(String line) {
        if (!spawned) {
            return this;
        }

        this.lines.put(++current,
                new HologramLine(this, location.clone().add(0, 0.33 * current, 0), ChatUtil.format(line)));
        return this;
    }

    @Override
    public IHologram updateLine(int id, String line) {
        if (!lines.containsKey(id)) {
            return this;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            updateLine(PlayerManager.getManager().getGamePlayer(player.getUniqueId()), id, line);
        }

        return this;
    }

    @Override
    public IHologram updateLine(World world, int id, String line) {
        if (!lines.containsKey(id)) {
            return this;
        }

        for (Player player : world.getPlayers()) {
            updateLine(PlayerManager.getManager().getGamePlayer(player.getUniqueId()), id, line);
        }

        return this;
    }

    @Override
    public IHologram updateLine(IGamePlayer player, int id, String line) {
        if (!lines.containsKey(id)) {
            return this;
        }

        HologramLine hl = lines.get(id);
        hl.setLine(player, ChatUtil.format(line));
        return this;
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public List<IHologramLine> getLines() {
        return new ArrayList<>(lines.values());
    }
}
