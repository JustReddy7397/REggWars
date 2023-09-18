package ga.justreddy.wiki.reggwars.api.model.game.generator;

/**
 * @author JustReddy
 */
public enum GeneratorType {

    IRON, GOLD, DIAMOND, EMERALD;

    public static GeneratorType getType(String type) {
        for (GeneratorType t : values()) {
            if (t.name().equalsIgnoreCase(type)) return t;
        }
        return null;
    }

}
