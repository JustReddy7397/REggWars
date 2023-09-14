package ga.justreddy.wiki.reggwars.api.model.language;

/**
 * @author JustReddy
 */

public enum Message {


    ENUMS_COSMETICS_UNLOCKED("enums.cosmetics.unlocked"),
    ENUMS_COSMETICS_LOCKED("enums.cosmetics.locked"),
    ENUMS_COSMETICS_SELECTED("enums.cosmetics.selected"),
    ENUMS_COSMETICS_NOT_SELECTED("enums.cosmetics.not-selected"),
    ENUMS_COSMETICS_CANT_SELECT("enums.cosmetics.cant-select");
    ;

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
