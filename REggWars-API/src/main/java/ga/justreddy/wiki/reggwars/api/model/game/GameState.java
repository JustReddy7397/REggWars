package ga.justreddy.wiki.reggwars.api.model.game;

import java.io.Serializable;

/**
 * @author JustReddy
 */
public enum GameState implements Serializable {

    WAITING,
    STARTING,
    PLAYING,
    ENDING,
    RESTARTING,
    DISABLED

}
