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

    IGamePlayer getGamePlayer(UUID uuid);

    IGame getGame(String name);

    void registerVictoryDance(VictoryDance dance);

    void registerKillEffect(KillEffect effect);

    void registerKillMessage(KillMessage message);

}
