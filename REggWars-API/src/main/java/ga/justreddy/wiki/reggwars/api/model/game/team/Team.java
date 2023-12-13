package ga.justreddy.wiki.reggwars.api.model.game.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;

/**
 * @author JustReddy
 */
public enum Team {

    RED("red", format("&cRed"), format("&c[RED]"), Color.RED, format("&c&lRed"), format("&c[R] "), 1),
    BLUE("blue", format("&9Blue"), format("&9[BLUE]"), Color.BLUE, format("&9&lBlue"), format("&9[B] ") ,2),
    GREEN("green", format("&aGreen"), format("&a[GREEN]"), Color.LIME, format("&a&lGreen"), format("&a[G] ") ,3),
    YELLOW("yellow", format("&eYellow"), format("&e[YELLOW]"), Color.YELLOW, format("&e&lYellow"), format("&e[Y] ") ,4),
    AQUA("aqua", format("&bAqua"), format("&b[AQUA]"), Color.AQUA, format("&b&lAqua"), format("&b[A] ") ,5),
    WHITE("white", format("&fWhite"), format("&f[WHITE]"), Color.WHITE, format("&f&lWhite"), format("&f[W] ") ,6),
    PINK("pink", format("&dPink"), format("&d[PINK]"), Color.FUCHSIA, format("&d&lPink"), format("&d[P] ") ,7),
    GRAY("gray", format("&7Gray"), format("&7[GRAY]"), Color.GRAY, format("&7&lGray"), format("&7[G] ") ,8);


    private final String identifier;
    private final String displayName;
    private final String chatName;
    private final Color color;
    private final String bold;
    private final String tag;
    private final int weight;

    Team(String identifier, String displayName, String chatName, Color color, String bold, String tag, int weight) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.chatName = chatName;
        this.color = color;
        this.bold = bold;
        this.tag = tag;
        this.weight = weight;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getChatName() {
        return chatName;
    }

    public Color getColor() {
        return color;
    }

    public String getBold() {
        return bold;
    }

    public String getTag() {
        return tag;
    }

    public int getWeight() {
        return weight;
    }

    public static Team getByIdentifier(String identifier) {
        for (Team team : Team.values()) {
            if (team.getIdentifier().equalsIgnoreCase(identifier)) return team;
        }
        return null;
    }

    private static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }


}
