package ga.justreddy.wiki.reggwars.packets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author JustReddy
 */
public class TeamWrapper {

    private final Object packet = TeamAccessor.createPacket();

    public TeamWrapper(String name, int param, List<String> members) {
        if (param != 3 && param != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }

        setupDefaults(name, param);
        setupMembers(members);
    }

    public TeamWrapper(String name, String prefix, String suffix, int param, Collection<?> players) {
        setupDefaults(name, param);
        if (param == 0 || param == 2) {
            try {
                TeamAccessor.DISPLAY_NAME.set(packet, name);
                TeamAccessor.PREFIX.set(packet, prefix);
                TeamAccessor.SUFFIX.set(packet, suffix);
                TeamAccessor.PACK_OPTION.set(packet, 1);
                if (param == 0) {
                    ((Collection) TeamAccessor.MEMBERS.get(packet)).addAll(players);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupMembers(Collection<?> players) {
        try {
            players = players == null || players.isEmpty() ? new ArrayList<>() : players;
            ((Collection) TeamAccessor.MEMBERS.get(packet)).addAll(players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDefaults(String name, int param) {
        try {
            TeamAccessor.TEAM_NAME.set(packet, name);
            TeamAccessor.PARAM_INT.set(packet, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send() {
        TeamAccessor.sendPacket(Bukkit.getOnlinePlayers(), packet);
    }

    public void send(Collection<? extends Player> players) {
        TeamAccessor.sendPacket(players, packet);
    }

    public void send(Player player) {
        TeamAccessor.sendPacket(player, packet);
    }


}
