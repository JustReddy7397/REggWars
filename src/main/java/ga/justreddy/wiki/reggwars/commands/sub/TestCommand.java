package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import ga.justreddy.wiki.reggwars.model.game.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * @author JustReddy
 */
public class TestCommand extends Command {

    public TestCommand(REggWars plugin) {
        super(plugin);
        setName("test");
        setDescription("Test Command");
        setSyntax("/eggwars test");
        setAliases(new ArrayList<>());
    }

    @Override
    public void onCommand(IGamePlayer g, String[] args) {
        g.getLocation().getWorld().dropItemNaturally(
                g.getLocation().add(0.5, 0, 0.5), new ItemStack(Material.IRON_INGOT)
        );
/*
        IGameTeam red = new GameTeam("white");
        IGameTeam blue = new GameTeam("gray");
        for (Player player : Bukkit.getOnlinePlayers()) {
            IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
            if (red.getPlayers().isEmpty() && !blue.hasPlayer(gamePlayer)) {
                red.addPlayer(gamePlayer);
            }
            if (blue.getPlayers().isEmpty() && !red.hasPlayer(gamePlayer)) {
                blue.addPlayer(gamePlayer);
            }
        }
        REggWars.getInstance().getNms().setTeamName(red);
        REggWars.getInstance().getNms().setTeamName(blue);
*/
    }
}
