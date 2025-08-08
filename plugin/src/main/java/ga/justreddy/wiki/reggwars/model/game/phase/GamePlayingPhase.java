package ga.justreddy.wiki.reggwars.model.game.phase;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.events.EggWarsGameStartEvent;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.generator.IGenerator;
import ga.justreddy.wiki.reggwars.model.game.Game;
import ga.justreddy.wiki.reggwars.utils.player.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public class GamePlayingPhase extends GamePhase {

    @Override
    public void onEnable(Game game) {
        game.setGameState(GameState.PLAYING);
        super.onEnable(game);
        game.getTeamAssigner().assignTeam(game);
        EggWarsGameStartEvent event = new EggWarsGameStartEvent(game);
        event.call();
        game.getGenerators().forEach(IGenerator::start);
        game.getShops().forEach((location, shop) -> shop.spawn(location));
        game.getTeams().forEach(team -> {
            game.toSpawn(team);
            if (team.getSize() == 0) {
                team.setEggGone(true);
                return;
            }
            team.getEggLocation().getBlock().setType(Material.DRAGON_EGG);
            REggWars.getInstance().getNms().setTeamName(team);
        });

        if (game.getLobbyLocation() != null && game.getLobbyCuboid() != null) {
            game.getLobbyCuboid().clear();
        }

        for (IGamePlayer gamePlayer : game.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            PlayerUtil.clearInventory(player);
        }

    }

    @Override
    public void onTick(Game game) {

        if (game.getAliveTeams().isEmpty()) {
            game.goToNextPhase();
        } else if (game.getAliveTeams().size() == 1) {
            game.getGamePhaseManager().setPhase(new GameEndPhase(game.getAliveTeams().get(0)));
        }

        game.getGenerators().forEach(generator -> {
            generator.getGameSign().update();
        });

    }

    @Override
    public void onDisable(Game game) {

    }

    @Override
    public GamePhase getNextPhase() {
        return new GameEndPhase(null);
    }
}
