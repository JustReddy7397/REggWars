package ga.justreddy.wiki.reggwars.api.model.game.generator;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import org.bukkit.Location;
import org.bukkit.Material;
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


    void setDelay(int delay);

    int getDelay();

    void setSpawnAmount(int amount);

    int getSpawnAmount();

    void setMaxAmount(int amount);

    int getMaxAmount();

    void destroy();

}
