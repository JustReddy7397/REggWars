package ga.justreddy.wiki.reggwars.model.game.signs;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * @author JustReddy
 */
public class GeneratorSign implements IGameSign {

    private final String id;
    private final Location location;
    private final IGame game;
    private final IGenerator generator;

    public GeneratorSign(String id, Location location, IGame game, IGenerator generator) {
        this.id = id;
        this.location = location;
        this.game = game;
        this.generator = generator;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public void update() {
        Sign sign = (Sign) location.getBlock().getState();
        if (!sign.getChunk().isLoaded()) {
            sign.getChunk().load(true);
        }

        sign.setLine(0, ChatUtil.format("&7[&dGenerator&7]"));
        String generator = "";
        switch (this.generator.getType()) {
            case IRON:
                generator = "&fIRON";
                break;
            case GOLD:
                generator = "&6GOLD";
                break;
            case DIAMOND:
                generator = "&bDIAMOND";
                break;
            case EMERALD:
                generator = "&2EMERALD";
                break;
        }
        sign.setLine(1, ChatUtil.format(generator));
        sign.setLine(2, ChatUtil.format("&7Level: &d" + this.generator.getLevel()));
        sign.setLine(3, ChatUtil.format(
                this.generator.isMaxLevel() ? "&cMax Level" : "&aClick to upgrade"
        ));
        sign.update(true);
    }

    @Override
    public Block getRelative() {
        return REggWars.getInstance().getNms().getRelative(location);
    }
}
