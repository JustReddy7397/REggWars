package ga.justreddy.wiki.reggwars.model.language;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.api.model.language.Replaceable;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Data
public class Language implements ILanguage {

    FileConfiguration config;
    String id;

    @Override
    public void sendMessage(IGamePlayer gamePlayer, Message message) {
        sendMessage(gamePlayer, message, new Replaceable("", ""));
    }

    @Override
    public void sendMessage(IGamePlayer gamePlayer, Message message, Replaceable... replaceables) {
        Player player = gamePlayer.getPlayer();
        if (player == null) return;
        String path = getString(message);
        for (Replaceable ra : replaceables) {
            if (ra.getKey().isEmpty() && ra.getValue().isEmpty()) continue;
            path = path.replaceAll(ra.getKey(), ra.getValue());
        }
        if (path.startsWith("json:")) {
            REggWars.getInstance().getNms().sendJsonMessage(player,
                    path.replaceAll("json:", "")
            );
            return;
        }
        player.sendMessage(ChatUtil.format(path));

    }

    @Override
    public void sendMessage(CommandSender sender, Message message) {
        sendMessage(sender, message, new Replaceable("", ""));
    }

    @Override
    public void sendMessage(CommandSender sender, Message message, Replaceable... replaceables) {
        String path = getString(message);
        for (Replaceable ra : replaceables) {
            if (ra.getKey().isEmpty() && ra.getValue().isEmpty()) continue;
            path = path.replaceAll(ra.getKey(), ra.getValue());
        }
        sender.sendMessage(ChatUtil.format(path));
    }

    @Override
    public void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle) {
        sendTitle(gamePlayer, title, subTitle, new Replaceable("", ""));
    }

    @Override
    public void sendTitle(IGamePlayer gamePlayer, Message title, Message subTitle, Replaceable... replaceables) {
        Player player = gamePlayer.getPlayer();
        if (player == null) return;
        String stringTitle = getString(title);
        for (Replaceable ra : replaceables) {
            if (ra.getKey().isEmpty() && ra.getValue().isEmpty()) continue;
            stringTitle = stringTitle.replaceAll(ra.getKey(), ra.getValue());
        }

        String stringSubTitle = getString(title);
        for (Replaceable ra : replaceables) {
            if (ra.getKey().isEmpty() && ra.getValue().isEmpty()) continue;
            stringSubTitle = stringSubTitle.replaceAll(ra.getKey(), ra.getValue());
        }

        REggWars.getInstance().getNms().sendTitle(player,
                ChatUtil.format(stringTitle),
                ChatUtil.format(stringSubTitle));


    }

    @Override
    public void sendActionBar(IGamePlayer gamePlayer, Message actionbar) {
        sendActionBar(gamePlayer, actionbar, new Replaceable("", ""));
    }

    @Override
    public void sendActionBar(IGamePlayer gamePlayer, Message actionbar, Replaceable... replaceables) {
        Player player = gamePlayer.getPlayer();
        if (player == null) return;
        String message = getString(actionbar);

        for (Replaceable ra : replaceables) {
            if (ra.getKey().isEmpty() && ra.getValue().isEmpty()) continue;
            message = message.replaceAll(ra.getKey(), ra.getValue());
        }

        REggWars.getInstance().getNms().sendActionbar(player, ChatUtil.format(message));

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
