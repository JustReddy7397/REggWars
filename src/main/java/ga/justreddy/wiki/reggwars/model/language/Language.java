package ga.justreddy.wiki.reggwars.model.language;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Data
public class Language implements ILanguage {

    FileConfiguration config;

    @Override
    public void sendMessage(IGamePlayer gamePlayer, Message message) {
        gamePlayer.getPlayer().sendMessage(ChatUtil.format(getString(message)));
    }

    @Override
    public void sendMessage(CommandSender sender, Message message) {
        sender.sendMessage(ChatUtil.format(config.getString(getString(message))));
    }

    @Override
    public void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle) {
        // TODO
    }

    @Override
    public void sendActionBar(IGamePlayer gamePlayer, Message actionbar) {
        // TODO
    }

    @Override
    public String getString(Message message) {
        return config.getString(message.getPath());
    }

    @Override
    public List<String> getStringList(Message message) {
        return config.getStringList(message.getPath());
    }
}
