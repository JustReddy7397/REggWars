package ga.justreddy.wiki.reggwars.utils;

import com.cryptomorin.xseries.XMaterial;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.plugin.loaders.LoaderUtils;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeWorldHasher;
import ga.justreddy.wiki.reggwars.utils.md5.MD5;
import lombok.SneakyThrows;
import org.bukkit.Color;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author JustReddy
 */
public class Util {

    @SneakyThrows
    public static SlimeWorldHasher hashWorld(File file) {
        System.out.println(file);
        if (!file.exists()) return null;
        byte[] data = Files.readAllBytes(file.toPath());
        String hashcode = hash(data);
        String name = file.getName().replace(".slime", "");
        return new SlimeWorldHasher(hashcode, name, data, "slime");
    }

    @SneakyThrows
    public static File downloadWorld(SlimeWorldHasher hasher) {
        File destination = new File("slime_worlds");
        if (!destination.exists()) destination.mkdir();
        File file = new File("slime_worlds/" + hasher.getName() + ".slime");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        Files.write(Paths.get(file.getAbsolutePath()), hasher.getData());
        return file;
    }

    public static String hash(byte[] data) {
        try {
            MD5 md5 = new MD5();
            String hashFile = MD5.asHex(data);
            md5.Update(hashFile, null);
            return md5.asHex();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static SlimeWorld loadWorld(File file) throws IOException, CorruptedWorldException, NewerFormatException {
        byte[] data = Files.readAllBytes(file.toPath());
        SlimePropertyMap spm = new SlimePropertyMap();
        spm.setString(SlimeProperties.WORLD_TYPE, "flat");
        spm.setInt(SlimeProperties.SPAWN_X, 0);
        spm.setInt(SlimeProperties.SPAWN_Y, 30);
        spm.setInt(SlimeProperties.SPAWN_Z, 0);
        spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        spm.setString(SlimeProperties.DIFFICULTY, "easy");
        spm.setBoolean(SlimeProperties.PVP, true);
        return LoaderUtils.deserializeWorld(REggWars.getInstance().getLoader(), file.getName().replace(".slime", ""), data, spm, true);
    }

    public static void updateGame(IGame game) {
        BungeeGame bungeeGame = new BungeeGame(
                game.getName(),
                REggWars.getInstance().getServerName(),
                game.getMaxPlayers(),
                game.getGameState(),
                game.getGameMode(),
                game.getPlayerNames()
        );
        REggWars.getInstance().getMessenger().getSender().sendUpdateGamePacket(bungeeGame);
    }

    public static XMaterial getWoolMaterialByColor(Color teamColor) {
        if (teamColor == Color.RED) {
            return XMaterial.RED_WOOL;
        }
        if (teamColor == Color.BLUE) {
            return XMaterial.BLUE_WOOL;
        }
        if (teamColor == Color.GREEN) {
            return XMaterial.GREEN_WOOL;
        }
        if (teamColor == Color.YELLOW) {
            return XMaterial.YELLOW_WOOL;
        }
        if (teamColor == Color.AQUA) {
            return XMaterial.LIGHT_BLUE_WOOL;
        }
        if (teamColor == Color.PURPLE) {
            return XMaterial.PURPLE_WOOL;
        }
        if (teamColor == Color.WHITE) {
            return XMaterial.WHITE_WOOL;
        }
        if (teamColor == Color.BLACK) {
            return XMaterial.BLACK_WOOL;
        }
        if (teamColor == Color.ORANGE) {
            return XMaterial.ORANGE_WOOL;
        }

        if (teamColor == Color.GRAY) {
            return XMaterial.GRAY_WOOL;
        }

        if (teamColor == Color.LIME) {
            return XMaterial.LIME_WOOL;
        }

        if (teamColor == Color.MAROON) {
            return XMaterial.RED_WOOL;
        }

        if (teamColor == Color.NAVY) {
            return XMaterial.BLUE_WOOL;
        }

        if (teamColor == Color.OLIVE) {
            return XMaterial.GREEN_WOOL;
        }

        if (teamColor == Color.SILVER) {
            return XMaterial.LIGHT_GRAY_WOOL;
        }

        if (teamColor == Color.TEAL) {
            return XMaterial.CYAN_WOOL;
        }

        if (teamColor == Color.FUCHSIA) {
            return XMaterial.PINK_WOOL;
        }

        return XMaterial.WHITE_WOOL;
    }

}
