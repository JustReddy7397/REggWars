package ga.justreddy.wiki.reggwars.api.model.language;

/**
 * @author JustReddy
 */

public enum Message {
    ;

    private final String path;

    Message(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
