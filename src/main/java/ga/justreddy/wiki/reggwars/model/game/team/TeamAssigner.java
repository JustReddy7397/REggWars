package ga.justreddy.wiki.reggwars.model.game.team;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.ITeamAssigner;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JustReddy
 */
public class TeamAssigner implements ITeamAssigner {

    private final LinkedList<IGamePlayer> skip = new LinkedList<>();


    @Override
    public void assignTeam(IGame game) {
        if (game.getPlayerCount() > game.getTeamSize() && game.getTeamSize() > 1) {
            LinkedList<List<IGamePlayer>> teams = new LinkedList<>();
            if (!teams.isEmpty()) {
                for (IGameTeam team : game.getTeams()) {
                    teams.sort(Comparator.comparing(List::size));
                    if (teams.get(0).isEmpty()) break;
                    for (int i = 0; i < game.getTeamSize() && team.getPlayers().size() < game.getTeamSize(); i++) {
                        if (teams.get(0).size() > i) {
                            IGamePlayer toAdd = teams.get(0).remove(0);
                            toAdd.getPlayer().closeInventory();
                            team.addPlayer(toAdd);
                            skip.add(toAdd);
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        for (IGamePlayer remaining : game.getPlayers()) {
            if (skip.contains(remaining)) continue;
            for (IGameTeam team : game.getTeams()) {
                if (team.getPlayers().size() < game.getTeamSize()) {
                    remaining.getPlayer().closeInventory();
                    team.addPlayer(remaining);
                    break;
                }
            }
        }

    }
}
