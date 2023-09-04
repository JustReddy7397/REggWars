package ga.justreddy.wiki.reggwars.model.game.generator;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.generator.GeneratorType;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author JustReddy
 */
public class Generator implements IGenerator {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public Material getMaterial() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void dropItem() {

    }

    @Override
    public IGame getGame() {
        return null;
    }

    @Override
    public GeneratorType getType() {
        return null;
    }

    @Override
    public void setDelay(int delay) {

    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void setSpawnAmount(int amount) {

    }

    @Override
    public int getSpawnAmount() {
        return 0;
    }

    @Override
    public void setMaxAmount(int amount) {

    }

    @Override
    public int getMaxAmount() {
        return 0;
    }

    @Override
    public void destroy() {

    }
}
