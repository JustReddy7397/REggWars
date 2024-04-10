package ga.justreddy.wiki.reggwars.utils;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.support.bungeemode.bungee.Bungee;
import ga.justreddy.wiki.reggwars.utils.iridium.IridiumColorAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author JustReddy
 */
public class ChatUtil {

    public static String format(String input) {
        return IridiumColorAPI.process(input);
    }

    public static void sendConsole(String s) {
        Bukkit.getConsoleSender().sendMessage(format(s));
    }

    public static void sendConsoleBungee(String s) {
        Bungee.getInstance().getProxy().getConsole().sendMessage(new TextComponent(format(s)));
    }

}
