package ga.justreddy.wiki.reggwars.storage.type;

import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author JustReddy
 */
public interface DataSender {

    void writeCount(Player optional, String serverType);

    void writeToLobby(Player player);

    void writeToGame(Player player, String serverType, String map);

    void writeToMapSelector(Player player, String serverType);

}
