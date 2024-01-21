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
    MESSAGES_SERVER_INVALID_ARGUMENTS("messages.server.invalid-arguments",
            "&cInvalid arguments! <usage>"),
    MESSAGES_SERVER_RELOADED("messages.server.reloaded",
            "&aSuccessfully reloaded the plugin!"),
    MESSAGES_SERVER_SPAWN_SET("messages.server.spawn-set",
            "&aSuccessfully set the spawn location!"),
    MESSAGES_SERVER_RESTARTED("messages.server.restarted",
            "&c&lServer is restarting, you have been disconnected."),
    MESSAGES_ARENA_HELP("messages.arena.help",
            "&d&lEggWars Arena Help",
            "&d/ew arena <subcommand>",
            "&d/ew arena help",
            "&d/ew arena create <name>",
            "&d/ew arena fstart",
            "&d/ew arena fstop",
            "&d/ew arena team <arguments>",
            "&d/ew arena waitinglobby",
            "&d/ew arena spectator",
            "&d/ew arena minplayers <amount>",
            "&d/ew arena save"
            ),
    MESSAGES_ARENA_CREATED("messages.arena.created",
            "&aSuccessfully created arena &d<name>"),
    MESSAGES_ARENA_ALREADY_EXISTS("messages.arena.already-exists",
            "&cAn arena with that name already exists!"),
    MESSAGES_ARENA_NOT_COMPLETED("messages.arena.not-completed",
            "&cYou must complete the arena before saving it!"),
    MESSAGES_ARENA_NOT_CREATING("messages.arena.not-creating",
            "&cYou are not creating an arena!"),
    MESSAGES_ARENA_NOT_FOUND("messages.arena.not-found",
            "&cArena not found!"),
    MESSAGES_ARENA_SAVED("messages.arena.saved",
            "&aSuccessfully saved the arena &d<name>"),
    MESSAGES_ARENA_TEAM_ADDED("messages.arena.team-added",
            "&aSuccessfully added team <team>"),
    MESSAGES_ARENA_TEAM_SET_EGG("messages.arena.team-set-egg",
            "&aSuccessfully set the egg for team <team>"),
    MESSAGES_ARENA_TEAM_SET_SPAWN("messages.arena.team-set-spawn",
            "&aSuccessfully set the spawn for team <team>"),
    MESSAGES_ARENA_WAITING_LOBBY_SET("messages.arena.waiting-lobby-set",
            "&aSuccessfully set the waiting lobby!"),
    MESSAGES_ARENA_SPECTATOR_SET("messages.arena.spectator-set",
            "&aSuccessfully set the spectator location!"),
    MESSAGES_ARENA_BOUND_ARENA_HIGH("messages.arena.bound-arena-high",
            "aSuccessfully set the high bound of the arena!"),
    MESSAGES_ARENA_BOUND_ARENA_LOW("messages.arena.bound-arena-low",
            "&aSuccessfully set the low bound of the arena!"),
    MESSAGES_ARENA_BOUND_LOBBY_HIGH("messages.arena.bound-lobby-high",
            "&aSuccessfully set the high bound of the lobby!"),
    MESSAGES_ARENA_BOUND_LOBBY_LOW("messages.arena.bound-lobby-low",
            "&aSuccessfully set the low bound of the lobby!"),
    MESSAGES_ARENA_GENERATOR_WRONG_TYPE("messages.arena.generator-wrong-type",
            "&cInvalid generator type!"),
    MESSAGES_ARENA_INVALID_GENERATOR_LEVEL("messages.arena.invalid-generator-level",
            "&cThe generator start level must be between 0-<level>!"),
    MESSAGES_ARENA_GENERATOR_ADDED("messages.arena.generator-added",
            "&aSuccessfully added a generator!");


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
