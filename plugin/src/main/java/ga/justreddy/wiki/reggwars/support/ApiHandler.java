package ga.justreddy.wiki.reggwars.support;

import ga.justreddy.wiki.reggwars.api.EggWarsAPI;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillEffect;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;

import java.util.UUID;

/**
 * @author JustReddy
 */
public class ApiHandler implements EggWarsAPI {

    @Override
    public IGamePlayer getGamePlayer(UUID uuid) {
        return PlayerManager.getManager().getGamePlayer(uuid);
    }

    @Override
    public IGame getGame(String name) {
        return GameManager.getManager().getGameByName(name);
    }

    @Override
    public void removeVictoryDance(int id) {
        DanceManager.getManager().unregister(id);
    }

    @Override
    public void removeKillEffect(int id) {
        // TODO
    }

    @Override
    public void removeKillMessage(int id) {
        // TODO
    }
}
