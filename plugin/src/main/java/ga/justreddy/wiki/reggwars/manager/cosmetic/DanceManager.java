package ga.justreddy.wiki.reggwars.manager.cosmetic;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.ClassUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class DanceManager {

    private static DanceManager manager;

    public static DanceManager getManager() {
        return manager == null ? manager = new DanceManager() : manager;
    }

    @Getter
    private final Map<Integer, VictoryDance> dances;
    @Getter
    private final File dancesFile;

    public DanceManager() {
        this.dances = new HashMap<>();
        this.dancesFile = new File(REggWars.getInstance().getDataFolder(), "dances");
        if (!dancesFile.exists()) dancesFile.mkdirs();
    }

    public void start() {
        File[] files = dancesFile.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.getName().endsWith(".jar")) continue;
            register(file);
        }

        ChatUtil.sendConsole("&7[&dREggWars&7] &aLoaded &l" + dances.size() + "&r &avictory dances");

    }

    @SneakyThrows
    public void register(File file) {
        for (Class<? extends VictoryDance> dances : ClassUtil.findClasses(file, VictoryDance.class)) {
            VictoryDance dance = dances.newInstance();
            if (this.dances.containsKey(dance.getId())) continue;
            this.dances.put(dance.getId(), dance);
            ChatUtil.sendConsole("&7[&dReggWars&7] &aRegistered the &l" + dance.getSubname() + " " +
                    "(" + dance.getId() + ") &avictory dance");
        }
    }

    public void register(VictoryDance dance) {
        dances.put(dance.getId(), dance);
        ChatUtil.sendConsole("&7[&dReggWars&7] &aRegistered the &l" + dance.getSubname() + " " +
                "(" + dance.getId() + ") &avictory dance");
    }

    public void unregister(int id) {
        dances.remove(id);
    }

    public VictoryDance getById(int id) {
        return dances.getOrDefault(id, null);
    }

    public VictoryDance getByPermission(String permission) {
        for (VictoryDance dance : dances.values()) {
            if (dance.getPermission().equalsIgnoreCase(permission)) return dance;
        }
        return null;
    }

}
