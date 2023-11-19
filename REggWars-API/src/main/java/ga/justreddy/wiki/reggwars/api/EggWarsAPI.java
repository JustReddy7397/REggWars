package ga.justreddy.wiki.reggwars.api;

import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillEffect;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;

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
     * Register a victory dance
     * @param dance the victory dance to register
     */
    void registerVictoryDance(VictoryDance dance);

    /**
     * Register a kill effect
     * @param effect the kill effect to register
     */
    void registerKillEffect(KillEffect effect);

    /**
     * Register a kill message
     * @param message the kill message to register
     */
    void registerKillMessage(KillMessage message);

}
