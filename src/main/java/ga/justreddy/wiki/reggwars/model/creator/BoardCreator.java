package ga.justreddy.wiki.reggwars.model.creator;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.board.lib.FastBoard;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@Getter
public abstract class BoardCreator {

    private final FastBoard fastBoard;

    public BoardCreator(IGamePlayer gamePlayer) {
        fastBoard = new FastBoard(gamePlayer.getPlayer());;
    }

    public void setTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        fastBoard.updateTitle(title);
    }

    public void setSlot(int slot, String text) {
        text = ChatColor.translateAlternateColorCodes('&', placeholder(text));
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }
        fastBoard.updateLine(slot, text);
    }

    public void setLines(String... lines) {
        List<String> list = new ArrayList<>();
        for (String line : lines) {
            if (line.length() > 32) {
                line = line.substring(0, 32);
            }
            list.add(ChatUtil.format(placeholder(line)));
        }
        fastBoard.updateLines(list);
    }

    public void setLines(List<String> lines) {
        List<String> list = new ArrayList<>();
        for (String line : lines) {
            if (line.length() > 32) {
                line = line.substring(0, 32);
            }
            list.add(ChatUtil.format(placeholder(line)));
        }
        fastBoard.updateLines(list);
    }

    public void setLine(int index, String text) {
        text = ChatUtil.format(placeholder(text));

        if (text.length() > 32) {
            text = text.substring(0, 32);
        }

        fastBoard.updateLine(index, text);
    }

    public void removeSlot(int slot) {
        fastBoard.removeLine(slot);
    }

    public abstract String placeholder(String text);



}
