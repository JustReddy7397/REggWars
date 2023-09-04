package ga.justreddy.wiki.reggwars.api.model.language;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author JustReddy
 */
public interface ILanguage {

    void sendMessage(IGamePlayer gamePlayer, Message message);

    void sendMessage(CommandSender sender, Message message);

    void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle);

    void sendActionBar(IGamePlayer gamePlayer, Message actionbar);

    String getString(Message message);

    List<String> getStringList(Message message);

}
