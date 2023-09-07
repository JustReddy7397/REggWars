package ga.justreddy.wiki.reggwars.model.game.generator;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author JustReddy
 */
public class Generator implements IGenerator {

    private final String id;
    private final int startLevel;
    private final int maxLevel;
    private final Material material;
    private final Location location;
    private final IGame game;
    private final GeneratorType type;
    private int spawnAmount = 0;
    private int maxSpawnAmount = 0;
    private int delay;
    private int upgradeCost;
    private BukkitTask task;

    public Generator(String id,
                     int startLevel,
                     int maxLevel,
                     Material material, Location location, IGame game, GeneratorType type) {
        this.id = id;
        this.startLevel = startLevel;
        this.maxLevel = maxLevel;
        this.material = material;
        this.location = location;
        this.game = game;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void upgrade() {
        if (task != null) task.cancel();

        task = Bukkit.getScheduler().runTaskTimer(REggWars.getInstance(), this::dropItem, 0, delay);
    }

    @Override
    public void dropItem() {
        location.getWorld().dropItemNaturally(location, new ItemStack(material, spawnAmount));
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public GeneratorType getType() {
        return type;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setSpawnAmount(int amount) {
        this.spawnAmount = amount;
    }

    @Override
    public int getSpawnAmount() {
        return spawnAmount;
    }

    @Override
    public void setMaxAmount(int amount) {
        this.maxSpawnAmount = amount;
    }

    @Override
    public int getMaxAmount() {
        return maxSpawnAmount;
    }

    @Override
    public int getUpgradeCost() {
        return upgradeCost;
    }

    @Override
    public void setUpgradeCost(int cost) {
        this.upgradeCost = cost;
    }

    @Override
    public void destroy() {
        if (task != null) task.cancel();
    }

    @Override
    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(REggWars.getInstance(), this::dropItem, 0, delay);
    }
}
