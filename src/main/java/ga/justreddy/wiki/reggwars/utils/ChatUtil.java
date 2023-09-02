package ga.justreddy.wiki.reggwars.utils;

import org.bukkit.ChatColor;

/**
 * @author JustReddy
 */
public class ChatUtil {

    public static String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
