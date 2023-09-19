package ga.justreddy.wiki.reggwars.utils;

import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;

/**
 * @author JustReddy
 */
public class NumberUtil {

    public static int getPriority(IGameTeam team) {
        if (team == null) return 0;
        switch (team.getTeam()) {
            case RED:
                return 1;
            case BLUE:
                return 2;
            case GREEN:
                return 3;
            case YELLOW:
                return 4;
            case AQUA:
                return 5;
            case WHITE:
                return 6;
            case PINK:
                return 7;
            case GRAY:
                return 8;
        }
        return 0;
    }

}
