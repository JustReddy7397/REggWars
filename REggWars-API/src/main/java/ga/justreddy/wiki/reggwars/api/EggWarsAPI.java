package ga.justreddy.wiki.reggwars.api;

import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillEffect;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

import java.io.FileReader;
import java.util.UUID;

/**
 * @author JustReddy
 */
public interface EggWarsAPI {

    /**
     * Get the game player by uuid
     * @param uuid the uuid
     * @return the game player
     */
    IGamePlayer getGamePlayer(UUID uuid);

    /**
     * Get the game by name
     * @param name the name
     * @return the game
     */
    IGame getGame(String name);

    /**
     * Remove a victory dance
     * If you want to remove the default victory dance, for example.
     * And make your own default victory dance with the id 0
     * @param id the id of the victory dance
     */
    void removeVictoryDance(int id);

    /**
     * Remove a kill effect
     * If you want to remove the default kill effect, for example.
     * And make your own default kill effect with the id 0
     * @param id the id of the kill effect
     */
    void removeKillEffect(int id);

    /**
     * Remove a kill message
     * If you want to remove the default kill message, for example.
     * And make your own default kill message with the id 0
     * @param id the id of the kill message
     */
    void removeKillMessage(int id);

    default String getVersion() {
        return "1.0.0";
    }

}
