package ga.justreddy.wiki.reggwars.api.model.language;

/**
 * @author JustReddy
 */

public enum Message {


    // Enums
    ENUMS_COSMETICS_UNLOCKED("enums.cosmetics.unlocked",
            "&aUnlocked"
            ),
    ENUMS_COSMETICS_LOCKED("enums.cosmetics.locked",
            "&cLocked"
            ),
    ENUMS_COSMETICS_SELECTED("enums.cosmetics.selected",
            "&aSelected"
            ),
    ENUMS_COSMETICS_NOT_SELECTED("enums.cosmetics.not-selected",
            "&aClick to select"
            ),
    ENUMS_COSMETICS_CANT_SELECT("enums.cosmetics.cant-select",
            "&cCost: &6<cost>"
            ),

    // Scoreboard
    SCOREBOARD_DATE_FORMAT("scoreboard.date-format",
            "dd/MM/yyyy"),
    SCOREBOARD_YOU_TEXT("scoreboard.you",
            "&7YOU"
            ),
    SCOREBOARD_DEAD_TEXT("scoreboard.dead",
            "&c&l✗"
    ),
    SCOREBOARD_ALIVE_TEXT("scoreboard.alive",
            "&a&l✔"
            ),
    SCOREBOARD_PLAYING_TITLE("scoreboard.game-board.title",
            "&d&lEggWars"
            ),
    SCOREBOARD_PLAYING_LINES("scoreboard.game-board.playing",
            "",
            "Waiting...",
            "",
            "&ejustreddy.tech"
            ),
    SCOREBOARD_WAITING_LINES("scoreboard.game-board.waiting",
            "",
            "<teams>",
            "",
            "&ejustreddy.tech"
            ),

    // Titles
    TITLES_RESPAWNING_TITLE("titles.respawn.title",
            "&cYOU DIED!"),
    TITLES_RESPAWNING_SUBTITLE("titles.respawn.subtitle",
            "&eYou will respawn in &c<time> &eseconds"),
    TITLES_DIED_TITLE("titles.died.title",
            "&cYOU DIED!"),
    TITLES_DIED_SUBTITLE("titles.died.subtitle",
            "&eBetter luck next time!"),

    // Messages
    MESSAGES_GAME_RESPAWN("messages.game.respawn",
            "&eYou will respawn in &c<time> &eseconds"),
    MESSAGES_GAME_CANT_BREAK_BLOCKS("messages.game.cant-break-blocks",
            "&cYou can only break blocks placed by players!"),
    MESSAGES_GAME_CANT_PLACE_BLOCKS_GENERATOR("messages.game.cant-place-blocks-generator",
            "&cYou can't place blocks on a generator"),
    MESSAGES_GAME_CANT_BREAK_OWN_EGG("messages.game.cant-break-own-egg",
            "&cYou can't break your own egg silly!"),
    MESSAGES_SERVER_RESTARTED("messages.server.restarted",
            "&c&lServer is restarting, you have been disconnected.");

    private final String path;
    private final String[] def;

    Message(String path, String... def) {
        this.path = path;
        this.def = def;
    }

    public String getPath() {
        return path;
    }

    public String[] getDef() {
        return def;
    }
}
