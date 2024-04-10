package ga.justreddy.wiki.reggwars.api.model.game;

import java.io.Serializable;

/**
 * @author JustReddy
 */
public enum GameMode implements Serializable {

    SOLO("SOLO"),
    TEAM("TEAM");

    private final String name;

    GameMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
