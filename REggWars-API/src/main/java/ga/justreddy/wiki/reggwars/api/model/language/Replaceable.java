package ga.justreddy.wiki.reggwars.api.model.language;

/**
 * @author JustReddy
 */
public class Replaceable {

    private final String key;
    private final String value;

    public Replaceable(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
