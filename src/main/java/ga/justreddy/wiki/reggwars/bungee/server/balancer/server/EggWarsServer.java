package ga.justreddy.wiki.reggwars.bungee.server.balancer.server;

import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import lombok.Getter;

/**
 * @author JustReddy
 */
@Getter
public class EggWarsServer extends BungeeServer {

    private String map;
    private GameState state;

    public EggWarsServer(String serverId, int maxPlayers, GameState state) {
        super(serverId, maxPlayers);
        this.state = state;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public boolean canBeSelected() {
        return super.canBeSelected() && !isInProgress();
    }

    public boolean isInProgress() {
        return state == GameState.PLAYING || state == GameState.ENDING || state == GameState.RESTARTING;
    }

}
