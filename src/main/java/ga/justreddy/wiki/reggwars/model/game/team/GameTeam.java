package ga.justreddy.wiki.reggwars.model.game.team;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author JustReddy
 */
public class GameTeam implements IGameTeam {

    private final String id;
    private final List<IGamePlayer> players;
    private final Location spawnLocation;
    private final Location eggLocation;
    private boolean eggGone = false;

    public GameTeam(String id, ConfigurationSection section) {
        this.id = id;
        this.players = new ArrayList<>();
        this.spawnLocation = LocationUtils.getLocation(section.getString("spawn"));
        this.eggLocation = LocationUtils.getLocation(section.getString("egg"));
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<IGamePlayer> getPlayers() {
        return players;
    }

    @Override
    public List<IGamePlayer> getAlivePlayers() {
        try (Stream<IGamePlayer> stream = players.stream()) {
            return stream.filter(player -> !player.isDead()).collect(Collectors.toList());
        }
    }

    @Override
    public List<IGamePlayer> getSpectatorPlayers() {
        try (Stream<IGamePlayer> stream = players.stream()) {
            return stream.filter(IGamePlayer::isDead).collect(Collectors.toList());
        }
    }

    @Override
    public void addPlayer(IGamePlayer player) {
        if (hasPlayer(player)) return;
        players.add(player);
    }

    @Override
    public boolean hasPlayer(IGamePlayer player) {
        return players.contains(player);
    }

    @Override
    public void removePlayer(IGamePlayer player) {
        if (!hasPlayer(player)) return;
        players.remove(player);
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public Location getEggLocation() {
        return eggLocation;
    }

    @Override
    public boolean isEggGone() {
        return eggGone;
    }

    @Override
    public void setEggGone(boolean eggGone) {
        this.eggGone = eggGone;
    }
}
