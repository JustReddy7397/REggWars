package ga.justreddy.wiki.reggwars.api.model.entity;

import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerCosmetics;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerQuests;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerQuickBuy;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerSettings;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
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

    boolean isFakeDead();

    void setFakeDead(boolean dead);

    boolean isSpectating();

    void setSpectating(boolean spectating);

    void sendLegacyMessage(String message);

    void sendMessage(Message message);

    void sendMessage(Message message, Replaceable... replaceables);

    void sendTitle(Message title, Message subTitle);

    void sendTitle(Message title, Message subTitle, Replaceable... replaceables);

    void sendActionBar(Message actionBar);

    void sendActionBar(Message actionBar, Replaceable... replaceables);

    void sendSound(String name);

    void teleport(Location location);

    Location getLocation();

    IPlayerSettings getSettings();

    void setSettings(IPlayerSettings settings);

    IPlayerCosmetics getCosmetics();

    void setCosmetics(IPlayerCosmetics cosmetics);

    ICombatLog getCombatLog();

    void setCombatLog(ICombatLog combatLog);

    IPlayerQuickBuy getQuickBuy();

    void setQuickBuy(IPlayerQuickBuy quickBuy);

    IPlayerQuests getQuests();

    void setQuests(IPlayerQuests quests);


}
