package ga.justreddy.wiki.reggwars.nms.v1_8_R3;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import ga.justreddy.wiki.reggwars.schematic.ISchematic;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
/**
 * @author JustReddy
 */
public class Schematic implements ISchematic {

    @Override
    public String getPrefix() {
        return ".schematic";
    }

    @Override
    public void save(File file, Location low, Location high) {
        try {
            BukkitWorld bukkitWorld = new BukkitWorld(low.getWorld());
            Vector vector1 = new Vector(low.getBlockX(), low.getBlockY(), low.getBlockZ());
            Vector vector2 = new Vector(high.getBlockX(), high.getBlockY(), high.getBlockZ());
            CuboidRegion region = new CuboidRegion(bukkitWorld, vector1, vector2);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            EditSession editSession = WorldEdit.getInstance()
                    .getEditSessionFactory().getEditSession(region.getWorld(), -1);
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            forwardExtentCopy.setRemovingEntities(true);
            Operations.complete(forwardExtentCopy);
            try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(Files.newOutputStream(file.toPath()))) {
                writer.write(clipboard, bukkitWorld.getWorldData());
            }
        }catch (WorldEditException | IOException e) {
            e.fillInStackTrace();
        }

    }

    @Override
    public Object get(File file, World world) {
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        ClipboardFormat format = ClipboardFormat.findByFile(file);
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()));
            return reader.read(bukkitWorld.getWorldData());
        }catch (IOException e) {
            e.fillInStackTrace();
        }
        return null;
    }

    @Override
    public void paste(Object schematic, Location location) {
        if (!(schematic instanceof Clipboard)) {
            throw new IllegalStateException("Schematic Object is not an instance of Clipboard");
        }
        Clipboard schem = (Clipboard) schematic;

        try {
            BukkitWorld bukkitWorld = new BukkitWorld(location.getWorld());
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, -1);
            Operation operation = new ClipboardHolder(schem, bukkitWorld.getWorldData())
                    .createPaste(editSession, bukkitWorld.getWorldData())
                    .to(new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .ignoreAirBlocks(true)
                    .build();
            Operations.complete(operation);
        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }
}
