package ga.justreddy.wiki.reggwars.utils;

import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.model.game.BungeeGame;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author JustReddy
 */
public class ShuffleUtil {

    public static void shuffleBungee(List<BungeeGame> games) {
        games.sort(Comparator.comparing(value -> value.getPlayers().size()));
        Collections.reverse(games);
        int startIndex = 0;
        int endIndex = 0;
        int currentValue = games.get(0).getPlayers().size();
        Random random = ThreadLocalRandom.current();
        for(int i = 1; i < games.size(); i++){
            BungeeGame currentObject = games.get(i);
            int value = currentObject.getPlayers().size();
            if(value == currentValue){
                endIndex = i;
            } else {
                shuffleEqualValuesBungee(games, startIndex, endIndex, random);
                startIndex = i;
                endIndex = i;
                currentValue = value;
            }
        }
        shuffleEqualValuesBungee(games, startIndex, endIndex, random);
    }

    private static void shuffleEqualValuesBungee(List<BungeeGame> list, int start, int end, Random random) {
        int size = end - start + 1;
        while(size > 1){
            int i = start + random.nextInt(size);
            int j = start + --size;
            Collections.swap(list, i, j);
        }
    }

    public static void shuffle(List<IGame> games) {

        games.sort(Comparator.comparingInt(IGame::getPlayerCount).reversed());

        int startIndex = 0;
        int endIndex = 0;
        int currentValue = games.get(0).getPlayerCount();
        Random random = ThreadLocalRandom.current();

        for(int i = 1; i < games.size(); i++){
            IGame currentObject = games.get(i);
            int value = currentObject.getPlayerCount();
            if(value == currentValue){
                endIndex = i;
            } else {
                shuffleEqualValues(games, startIndex, endIndex, random);
                startIndex = i;
                endIndex = i;
                currentValue = value;
            }
        }
        shuffleEqualValues(games, startIndex, endIndex, random);
    }

    private static void shuffleEqualValues(List<IGame> list, int start, int end, Random random) {
        int size = end - start + 1;
        while(size > 1){
            int i = start + random.nextInt(size);
            int j = start + --size;
            Collections.swap(list, i, j);
        }
    }


}
