package ga.justreddy.wiki.reggwars.model.game.generator;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.model.game.signs.GeneratorSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 * @author JustReddy
 */
public class Generator implements IGenerator {

    private final String id;
    private final int startLevel;
    private final int maxLevel;
    private final Material material;
    private Material upgradeMaterial;
    private final Location location;
    private final IGame game;
    private final GeneratorType type;
    private int level;
    private int spawnAmount = 0;
    private int maxSpawnAmount = 0;
    private int delay;
    private int upgradeCost;
    private BukkitTask task;
    private ConfigurationSection section;
    private final IGameSign sign;
    private final World world;
    private boolean shouldDrop = true;

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
        this.level = startLevel;
        FileConfiguration configuration = REggWars.getInstance().getGeneratorsConfig().getConfig();
        switch (type) {
            case IRON:
                this.section = configuration.getConfigurationSection("spawners.iron");
                break;
            case GOLD:
                this.section = configuration.getConfigurationSection("spawners.gold");
                break;
            case DIAMOND:
                this.section = configuration.getConfigurationSection("spawners.diamond");
                break;
            case EMERALD:
                this.section = configuration.getConfigurationSection("spawners.emerald");
                break;
        }
        String upgradeCost = section.getString(level + ".upgrade-cost", null);
        if (upgradeCost != null) {
            String[] split = section.getString(level + ".upgrade-cost").split(" ; ");
            setUpgradeCost(Integer.parseInt(split[0]));
            setUpgradeMaterial(getMaterial(split[1]));
        } else {
            setUpgradeCost(Integer.MAX_VALUE);
            setUpgradeMaterial(null);
        }
        this.sign = new GeneratorSign(id, location, game, this);
        this.delay = section.getInt(level + ".drop-time");
        this.spawnAmount = section.getInt(level + ".spawn-amount");
        this.maxSpawnAmount = section.getInt(level + ".max-dropped");
        this.world = game.getWorld();
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
        return sign.getRelative().getLocation();
    }

    @Override
    public void upgrade() {
        if (task != null) task.cancel();
        level += 1;
        if (level >= maxLevel) return;
        int delay = section.getInt(level + ".drop-time") * 20;
        setDelay(delay);
        int maxDropped = section.getInt(level + ".max-dropped");
        setMaxAmount(maxDropped);
        int spawnAmount = section.getInt(level + ".spawn-amount");
        setSpawnAmount(spawnAmount);
        String upgradeCost = section.getString(level + ".upgrade-cost", null);
        if (upgradeCost != null) {
            String[] split = section.getString(level + ".upgrade-cost").split(" ; ");
            setUpgradeCost(Integer.parseInt(split[0]));
            setUpgradeMaterial(getMaterial(split[1]));
        } else {
            setUpgradeCost(Integer.MAX_VALUE);
            setUpgradeMaterial(null);
        }

        task = Bukkit.getScheduler().runTaskTimer(REggWars.getInstance(), this::dropItem, 0, this.delay * 20L);
    }

    @Override
    public void dropItem() {
        if (location == null) return;
        int count = 0;
        for (Entity entity : world.getNearbyEntities(getLocation(), 2, 2, 2)) {
            if (!(entity instanceof Item)) continue;
            Item item = (Item) entity;
            if (item.getItemStack().getType() != getMaterial()) continue;
            count+=1;
        }
        if (count >= maxSpawnAmount) {
            shouldDrop = false;
        }
        if (!shouldDrop) {
            shouldDrop = true;
            return;
        }
        Vector vector = new Vector();
        Item item = location.getWorld().dropItemNaturally(getLocation().add(0.5, 1.0D, 0.5), new ItemStack(material));
        item.setVelocity(new Vector());
        shouldDrop = true;
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
    public Material getUpgradeMaterial() {
        return null;
    }

    @Override
    public void setUpgradeMaterial(Material material) {
        this.upgradeMaterial = material;
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
    public ConfigurationSection getSection() {
        return section;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        if (level > maxLevel) return;
        this.level = level;
    }

    @Override
    public void destroy() {
        if (task != null) task.cancel();
    }

    @Override
    public void start() {
        if (level == 0) return;
        task = Bukkit.getScheduler().runTaskTimer(REggWars.getInstance(), this::dropItem, 0, delay * 20L);
    }

    private Material getMaterial(String input) {
        switch (input) {
            case "IRON":
                return Material.IRON_INGOT;
            case "GOLD":
                return Material.GOLD_INGOT;
            case "DIAMOND":
                return Material.DIAMOND;
            case "EMERALD":
                return Material.EMERALD;
        }
        return null;
    }

    @Override
    public boolean isMaxLevel() {
        return level >= maxLevel;
    }

    @Override
    public IGameSign getGameSign() {
        return sign;
    }
}



