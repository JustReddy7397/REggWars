package ga.justreddy.wiki.reggwars.manager.cosmetic;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillEffect;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
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
public class KillMessageManager {

    private static KillMessageManager manager;

    public static KillMessageManager getManager() {
        return manager == null ? manager = new KillMessageManager() : manager;
    }

    @Getter
    private final Map<Integer, KillMessage> messages;
    @Getter
    private final File messagesFile;

    public KillMessageManager() {
        this.messages = new HashMap<>();
        this.messagesFile = new File(REggWars.getInstance().getDataFolder(), "killmessages");
        if (!messagesFile.exists()) messagesFile.mkdirs();
    }

    public void start() {
        File[] files = messagesFile.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.getName().endsWith(".jar")) continue;
            register(file);
        }

        ChatUtil.sendConsole("&7[&dREggWars&7] &aLoaded &l"+messages.size()+"&r &akill messages");

    }

    @SneakyThrows
    public void register(File file) {
        for (Class<? extends KillMessage> messages :
                ClassUtil.findClasses(file, KillMessage.class)) {
            KillMessage message = messages.newInstance();
            if (this.messages.containsKey(message.getId())) return;
            this.messages.put(message.getId(), message);
        }
    }

    public void register(KillMessage message) {
        messages.put(message.getId(), message);
    }

    public KillMessage getById(int id) {
        return messages.getOrDefault(id, null);
    }

}
