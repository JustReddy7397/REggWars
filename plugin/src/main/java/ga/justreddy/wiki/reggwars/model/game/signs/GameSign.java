package ga.justreddy.wiki.reggwars.model.game.signs;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.IGameSign;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 * @author JustReddy
 */
public class GameSign implements IGameSign {

    private final String id;
    private final Location location;
    private final IGame game;

    public GameSign(String id, Location location, IGame game) {
        this.id = id;
        this.location = location;
        this.game = game;
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
        Block block = location.getBlock();
        if (!(block.getState() instanceof Sign)) return;
        Sign sign = (Sign) block.getState();
        Block relative = getRelative();
        sign.setLine(0, ChatUtil.format("&7[&dREggWars&7]"));
        sign.setLine(1, ChatUtil.format("&7" + game.getDisplayName()));
        sign.setLine(2, ChatUtil.format("&7" + game.getPlayers().size() + "/" + game.getMaxPlayers()));
        switch (game.getGameState()) {
            case WAITING:
                sign.setLine(3, ChatUtil.format("&aWaiting"));
                relative.setType(XMaterial.GREEN_TERRACOTTA.parseMaterial());
                break;
            case STARTING:
                sign.setLine(3, ChatUtil.format("&eStarting"));
                relative.setType(XMaterial.YELLOW_TERRACOTTA.parseMaterial());
                break;
            case PLAYING:
                sign.setLine(3, ChatUtil.format("&cIn Game"));
                relative.setType(XMaterial.RED_TERRACOTTA.parseMaterial());
                break;
            case ENDING:
                sign.setLine(3, ChatUtil.format("&6Ending"));
                relative.setType(XMaterial.ORANGE_TERRACOTTA.parseMaterial());
                break;
            case DISABLED:
                sign.setLine(3, ChatUtil.format("&0Disabled"));
                relative.setType(XMaterial.BLACK_TERRACOTTA.parseMaterial());
                break;
            case RESTARTING:
                sign.setLine(3, ChatUtil.format("&9Restarting"));
                relative.setType(XMaterial.BLUE_TERRACOTTA.parseMaterial());
                break;
        }
        sign.update(true);
    }

    @Override
    public Block getRelative() {
        return REggWars.getInstance().getNms().getRelative(location);
    }
}
