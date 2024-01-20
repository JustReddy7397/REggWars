package ga.justreddy.wiki.reggwars.tasks;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;

/**
 * @author JustReddy
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PlayerSaveTask implements Runnable{

    Storage storage;

    @Override
    public void run() {
        for (IGamePlayer player : new ArrayList<>(PlayerManager.getManager().getPlayers())) {
            storage.savePlayer(player);
        }
    }
}
