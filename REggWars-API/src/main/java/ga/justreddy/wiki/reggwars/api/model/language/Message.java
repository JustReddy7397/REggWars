package ga.justreddy.wiki.reggwars.api.model.language;

/**
 * @author JustReddy
 */

public enum Message {


    // Enums
    ENUMS_COSMETICS_UNLOCKED("enums.cosmetics.unlocked"),
    ENUMS_COSMETICS_LOCKED("enums.cosmetics.locked"),
    ENUMS_COSMETICS_SELECTED("enums.cosmetics.selected"),
    ENUMS_COSMETICS_NOT_SELECTED("enums.cosmetics.not-selected"),
    ENUMS_COSMETICS_CANT_SELECT("enums.cosmetics.cant-select"),

    // Scoreboard
    SCOREBOARD_DATE_FORMAT("scoreboard.date-format"),
    SCOREBOARD_YOU_TEXT("scoreboard.you"),
    SCOREBOARD_DEAD_TEXT("scoreboard.dead"),
    SCOREBOARD_ALIVE_TEXT("scoreboard.alive"),
    SCOREBOARD_PLAYING_LINES("scoreboard.game-board.playing"),
    SCOREBOARD_WAITING_LINES("scoreboard.game-board.waiting"),

    // Titles
    TITLES_RESPAWNING_TITLE("titles.respawn.title"),
    TITLES_RESPAWNING_SUBTITLE("titles.respawn.subtitle"),
    TITLES_DIED_TITLE("titles.died.title"),
    TITLES_DIED_SUBTITLE("titles.died.subtitle"),

    // Messages
    MESSAGES_GAME_RESPAWN("messages.game.respawn");

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
