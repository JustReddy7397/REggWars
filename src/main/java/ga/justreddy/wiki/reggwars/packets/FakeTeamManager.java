package ga.justreddy.wiki.reggwars.packets;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
/**
 * @author JustReddy
 */
public class FakeTeamManager {

    private static final Map<String, FakeTeam> TEAMS = new HashMap<>();
    private static final Map<String, FakeTeam> CACHED_FAKE_TEAMS = new HashMap<>();
    private static final Map<UUID, List<FakeTeam>> PLAYER_TEAMS = new HashMap<>();

    public static Map<UUID, List<FakeTeam>> getPlayerTeams() {
        return PLAYER_TEAMS;
    }

    // - API

    public static void addTeam(FakeTeam team) {
        TEAMS.put(team.getName(), team);
    }

    public static void setTag(String p, String prefix, String suffix) {
        setTag(p, prefix, suffix, -1);
    }

    public static void setTag(String p, String prefix, String suffix, int sortPriority) {
        addPlayerToTeam(p, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
    }

    public static void setTag(Player toSend, String p, String prefix, String suffix, int sortPriority) {
        addPlayerToTeam(toSend, p, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
    }

    public static void sendTeams(Player p) {
        for (FakeTeam fakeTeam : TEAMS.values()) {
            new TeamWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0,
                    fakeTeam.getMembers()).send(p);
        }
    }

    public static void sendTeam(Player player, FakeTeam team) {
        new TeamWrapper(team.getName(), team.getPrefix(), team.getSuffix(), 0, team.getMembers()).send(player);
    }

    public static void reset() {
        for (FakeTeam fakeTeam : TEAMS.values()) {
            removePlayerFromTeamPackets(fakeTeam, fakeTeam.getMembers());
            removeTeamPackets(fakeTeam);
        }

        CACHED_FAKE_TEAMS.clear();
        TEAMS.clear();
    }



    // - Utility

    public static FakeTeam reset(String player) {
        return reset(player, decache(player));
    }

    private static FakeTeam decache(String player) {
        return CACHED_FAKE_TEAMS.remove(player);
    }

    public static FakeTeam getFakeTeam(String player) {
        return CACHED_FAKE_TEAMS.get(player);
    }

    private static void cache(String player, FakeTeam fakeTeam) {
        CACHED_FAKE_TEAMS.put(player, fakeTeam);
    }

    private static FakeTeam reset(String player, FakeTeam fakeTeam) {
        if (fakeTeam != null && fakeTeam.getMembers().remove(player)) {
            boolean delete;
            Player removing = Bukkit.getPlayerExact(player);
            if (removing != null) {
                delete = removePlayerFromTeamPackets(fakeTeam, removing.getName());
            } else {
                OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
                delete = removePlayerFromTeamPackets(fakeTeam, toRemoveOffline.getName());
            }

            if (delete) {
                removeTeamPackets(fakeTeam);
                TEAMS.remove(fakeTeam.getName());
            }
        }

        return fakeTeam;
    }

    public static void reset(Player player, FakeTeam team) {
        if (team.getMembers().remove(player.getName())) {
            removePlayerFromTeamPackets(team, player.getName());
        }
    }

    public static void reset(Player player, FakeTeam team, Collection<? extends Player> players) {
        if (team.getMembers().remove(player.getName())) {
            removePlayerFromTeamPackets(team, players, player.getName());
            removeTeamPackets(team);
        }
    }

    private static void addPlayerToTeam(String player, String prefix, String suffix,
                                        int sortPriority) {
        reset(player);
        FakeTeam joining = getTeam(prefix, suffix);
        if (joining != null) {
            joining.addMember(player);
        } else {
            joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
            joining.addMember(player);
            TEAMS.put(joining.getName(), joining);
            addTeamPackets(joining);
        }

        Player adding = Bukkit.getPlayerExact(player);
        if (adding != null) {
            addPlayerToTeamPackets(joining, adding.getName());
            cache(adding.getName(), joining);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
            addPlayerToTeamPackets(joining, offlinePlayer.getName());
            cache(offlinePlayer.getName(), joining);
        }
    }

    private static void addPlayerToTeam(Player toPlayer, String player, String prefix, String suffix,
                                        int sortPriority) {
        reset(player);
        FakeTeam joining = getTeam(prefix, suffix);
        if (joining != null) {
            joining.addMember(player);
        } else {
            joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
            joining.addMember(player);
            TEAMS.put(joining.getName(), joining);
            addTeamPackets(joining);
        }


        Player adding = Bukkit.getPlayerExact(player);
        if (adding != null) {
            addPlayerToTeamPackets(toPlayer, joining, adding.getName());
            cache(adding.getName(), joining);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
            addPlayerToTeamPackets(toPlayer, joining, offlinePlayer.getName());
            cache(offlinePlayer.getName(), joining);
        }
    }

    private static FakeTeam getTeam(String prefix, String suffix) {
        for (FakeTeam team : TEAMS.values()) {
            if (team.isSimilar(prefix, suffix)) {
                return team;
            }
        }

        return null;
    }

    private static String getNameFromInput(int input) {
        if (input < 0) {
            return "";
        }

        return  String.valueOf((char) ((input) + 65));
    }

    // -- Packets

    private static void removeTeamPackets(FakeTeam fakeTeam) {
        new TeamWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 1,
                new ArrayList<>()).send();
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, String... players) {
        return removePlayerFromTeamPackets(fakeTeam, Arrays.asList(players));
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, List<String> players) {
        new TeamWrapper(fakeTeam.getName(), 4, players).send();
        fakeTeam.getMembers().removeAll(players);
        return fakeTeam.getMembers().isEmpty();
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam team, List<String> players, List<Player> playerList) {
        new TeamWrapper(team.getName(), 4, players).send(playerList);
        team.getMembers().removeAll(players);
        return team.getMembers().isEmpty();
    }

    private static boolean removePlayerFromTeamPackets(FakeTeam team, Collection<? extends Player> playerList, String... players) {
        new TeamWrapper(team.getName(), 4, Arrays.asList(players)).send(playerList);
        team.getMembers().removeAll(Arrays.asList(players));
        return team.getMembers().isEmpty();
    }

    private static void addTeamPackets(FakeTeam fakeTeam) {
        new TeamWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0,
                fakeTeam.getMembers()).send();
    }

    private static void addPlayerToTeamPackets(FakeTeam fakeTeam, String player) {
        new TeamWrapper(fakeTeam.getName(), 3, Collections.singletonList(player)).send();
    }

    private static void addPlayerToTeamPackets(Player toPlayer, FakeTeam fakeTeam, String player) {
        new TeamWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(),0,  Collections.singletonList(player)).send(toPlayer);
    }


}
