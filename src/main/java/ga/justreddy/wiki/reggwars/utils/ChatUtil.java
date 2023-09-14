package ga.justreddy.wiki.reggwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author JustReddy
 */
public class ChatUtil {

    public static String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void sendConsole(String s) {
        Bukkit.getConsoleSender().sendMessage(format(s));
    }
}
