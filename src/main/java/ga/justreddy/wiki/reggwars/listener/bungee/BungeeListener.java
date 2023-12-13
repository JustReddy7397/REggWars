package ga.justreddy.wiki.reggwars.listener.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author JustReddy
 */

@SuppressWarnings("unchecked")
public class BungeeListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data) {
        if (!channel.equalsIgnoreCase("REggWarsAPI")) return;
   /*     ByteArrayDataInput input = ByteStreams.newDataInput(data);
        String subChannel = input.readUTF();

        if (subChannel.equalsIgnoreCase("Count")) {
            String type = input.readUTF();
            int count = input.readInt();
            try {
                Field field = ServerLobbies.class.getDeclaredField(type);
                field.setAccessible(true);
                field.set(null, count);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        } else if (subChannel.equalsIgnoreCase("MapSelector")) {
            String type = input.readUTF();
            try {
                Field field = ServerLobbies.class.getDeclaredField(type + "_MAP");
                field.setAccessible(true);
                Map<String, Integer> map = (Map<String, Integer>) field.get(null);
                map.clear();
                String entry = input.readUTF();
                map.put(entry.split(" : ")[0], Integer.parseInt(entry.split(" : ")[1]));
            } catch (Exception e) {
                // Ended
            }
        } else if (subChannel.equalsIgnoreCase("UpdateConfig")) {
            String name = input.readUTF();
            Bukkit.getScheduler().runTaskAsynchronously(REggWars.getInstance(), () -> {
               Storage storage = REggWars.getInstance().getStorage();
               storage.loadBungeeFiles(name);
            });
        }*/

    }
}
