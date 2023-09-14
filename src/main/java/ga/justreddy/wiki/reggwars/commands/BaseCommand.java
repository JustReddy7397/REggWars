package ga.justreddy.wiki.reggwars.commands;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.commands.sub.TestCommand;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class BaseCommand implements CommandExecutor {

    private final Map<String, ga.justreddy.wiki.reggwars.commands.Command> commands;

    public BaseCommand(REggWars plugin) {
        this.commands = new HashMap<>();
        register(new TestCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.format("&cOnly players may use the /eggwars command"));
            return true;
        }

        Player player = (Player) sender;

        IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());

        if (args.length == 0) {
            gamePlayer.sendLegacyMessage("&c/eggwars help");
            return true;
        }

        ga.justreddy.wiki.reggwars.commands.Command command = commands.getOrDefault(args[0], null);

        if (command == null) {
            return true;
        }

        if (command.getPermission() != null && !player.hasPermission(command.getPermission())) {
            // TODO
            return true;
        }

        command.onCommand(gamePlayer, args);

        return true;
    }

    private void register(ga.justreddy.wiki.reggwars.commands.Command command) {
        commands.put(command.getName(), command);
    }

    public Map<String, ga.justreddy.wiki.reggwars.commands.Command> getCommands() {
        return commands;
    }
}
