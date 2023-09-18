package ga.justreddy.wiki.reggwars.api.model.game.generator;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author JustReddy
 */
public interface IGenerator {

    String getId();

    int getStartLevel();

    int getMaxLevel();

    Material getMaterial();

    Location getLocation();

    void upgrade();

    void dropItem();

    IGame getGame();

    GeneratorType getType();

    Material getUpgradeMaterial();

    void setUpgradeMaterial(Material material);

    void setDelay(int delay);

    int getDelay();

    void setSpawnAmount(int amount);

    int getSpawnAmount();

    void setMaxAmount(int amount);

    int getMaxAmount();

    int getUpgradeCost();

    void setUpgradeCost(int cost);

    ConfigurationSection getSection();

    int getLevel();

    void setLevel(int level);

    void destroy();

    void start();

    boolean isMaxLevel();

    IGameSign getGameSign();

}
