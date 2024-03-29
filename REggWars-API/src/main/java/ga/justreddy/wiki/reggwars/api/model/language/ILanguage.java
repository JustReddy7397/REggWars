package ga.justreddy.wiki.reggwars.api.model.language;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

/**
 * @author JustReddy
 */
public interface ILanguage {

    /**
     * Send a message to a player
     * @param gamePlayer the player
     * @param message the message
     */
    void sendMessage(IGamePlayer gamePlayer, Message message);

    /**
     * Send a message to a player
     * @param gamePlayer the player
     * @param message the message
     * @param replaceables the replaceables
     */
    void sendMessage(IGamePlayer gamePlayer, Message message, Replaceable... replaceables);

    void sendMessage(CommandSender sender, Message message);

    void sendMessage(CommandSender sender, Message message, Replaceable... replaceables);

    void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle);

    void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle, Replaceable... replaceables);

    void sendActionBar(IGamePlayer gamePlayer, Message actionbar);

    void sendActionBar(IGamePlayer gamePlayer, Message actionbar, Replaceable... replaceables);

    String getString(Message message);

    List<String> getStringList(Message message);

    String getId();

    FileConfiguration getConfig();

    File getFile();

}
