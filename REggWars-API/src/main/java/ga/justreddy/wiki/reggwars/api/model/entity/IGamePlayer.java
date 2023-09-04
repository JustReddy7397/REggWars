package ga.justreddy.wiki.reggwars.api.model.entity;

import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerSettings;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface IGamePlayer {

    UUID getUniqueId();

    String getName();

    Player getPlayer();

    IGame getGame();

    void setGame(IGame game);

    IGameTeam getTeam();

    void setTeam(IGameTeam team);

    boolean isDead();

    void setDead(boolean dead);

    void sendLegacyMessage(String message);

    void sendMessage(Message message);

    void sendTitle(Message title, Message subTitle);

    void sendActionBar(Message actionBar);

    void sendSound(String name);

    void teleport(Location location);

    Location getLocation();

    IPlayerSettings getSettings();

    void setSettings(IPlayerSettings settings);


}
