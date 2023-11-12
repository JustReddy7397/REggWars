package ga.justreddy.wiki.reggwars.bungee.server.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import ga.justreddy.wiki.reggwars.bungee.server.ServerManager;
import ga.justreddy.wiki.reggwars.bungee.server.ServerType;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.server.BungeeServer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.server.EggWarsServer;
import ga.justreddy.wiki.reggwars.bungee.server.balancer.type.MostConnection;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class ServerListener implements Listener {

    private ServerManager manager;

    public ServerListener() {
        this.manager = ServerManager.getManager();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("REggWarsAPI") && event.getSender()
                instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
            ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
            String subChannel = input.readUTF();
            final ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
            final Server server = (Server) event.getSender();
            if (subChannel.startsWith("Lobby")) {
                BungeeServer bs = this.manager.getBalancer(ServerType.LOBBY).next();
                if (server != null) {
                    player.connect(bs.getServerInfo());
                } else {
                    player.sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Could not find a available lobby!"));
                }
            } else if (subChannel.startsWith("Count")) {
                ServerType type = null;
                try {
                    type = ServerType.valueOf(input.readUTF().toUpperCase());
                } catch (Exception ex) {
                    return;
                }

                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("Count");
                output.writeUTF(type.name());
                output.writeInt(this.manager.getBalancer(type).getTotalNumber());
                server.sendData("REggWarsAPI", output.toByteArray());
            } else if (subChannel.startsWith("Play")) {
                ServerType type = null;
                try {
                    type = ServerType.valueOf(input.readUTF().toUpperCase());
                } catch (Exception ex) {
                    return;
                }

                String mapFilter = input.readUTF();

                EggWarsServer es = (EggWarsServer) this.manager.getBalancer(type).next();
                if (!mapFilter.equalsIgnoreCase("all")) {
                    Map<String, BungeeServer> map = new HashMap<>();
                    for (BungeeServer s : manager.getBalancer(type).getList()) {
                        EggWarsServer ews = (EggWarsServer) s;
                        if (ews.getMap().equalsIgnoreCase(mapFilter)) {
                            map.put(ews.getServerInfo().getName(), ews);
                        }
                    }
                    MostConnection<BungeeServer> most = new MostConnection<>();
                    most.addAll(map);
                    es = (EggWarsServer) most.next();
                    most.destroy();
                }

                if (es != null) {
                    player.connect(es.getServerInfo());
                }

            } else if (subChannel.startsWith("MapSelector")) {
                ServerType type = null;
                try {
                    type = ServerType.valueOf(input.readUTF().toUpperCase());
                } catch (Exception ex) {
                    return;
                }

                Map<String, Integer> set = new HashMap<>();
                List<BungeeServer> servers = manager.getBalancer(type).getList();
                for (BungeeServer bs : servers) {
                    if (bs instanceof EggWarsServer) {
                        EggWarsServer es = (EggWarsServer) bs;
                        String map = es.getMap();
                        if (map != null) {
                            int current = set.get(map) != null ? set.get(map) : 0;
                            set.put(map, current + (bs.canBeSelected() ? 1 : 0));
                        }
                    }
                }

                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("MapSelector");
                output.writeUTF(type.name());
                for (Map.Entry<String, Integer> entry : set.entrySet()) {
                    output.writeUTF(entry.getKey() + " : " + entry.getValue());
                }

                set.clear();
                set = null;
                server.sendData("REggWarsAPI", output.toByteArray());
            }
        }
    }

}
