package ga.justreddy.wiki.reggwars.impl;

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
        return GameManager.getManager().getGameByName(name); // TODO
    }

    @Override
    public void registerVictoryDance(VictoryDance dance) {
        DanceManager.getManager().register(dance);
    }

    @Override
    public void registerKillEffect(KillEffect effect) {

    }

    @Override
    public void registerKillMessage(KillMessage message) {

    }
}
