package ga.justreddy.wiki.reggwars.model.entity;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerSettings;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author JustReddy
 */
public class GamePlayer implements IGamePlayer {

    private final UUID uniqueId;
    private final String name;
    private final Player player;

    private IGame game;
    private IGameTeam team;
    private boolean dead;

    public GamePlayer(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.player = Bukkit.getPlayer(uniqueId);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public void setGame(IGame game) {
        this.game = game;
    }

    @Override
    public IGameTeam getTeam() {
        return team;
    }

    @Override
    public void setTeam(IGameTeam team) {
        this.team = team;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public void sendLegacyMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
        ILanguage language = getSettings().getLanguage();
        language.sendMessage(this, message);
    }

    @Override
    public void sendTitle(Message title, Message subTitle) {

    }

    @Override
    public void sendActionBar(Message actionBar) {

    }

    @Override
    public void sendSound(String name) {

    }

    @Override
    public void teleport(Location location) {

    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public IPlayerSettings getSettings() {
        return null;
    }

    @Override
    public void setSettings(IPlayerSettings settings) {

    }
}
