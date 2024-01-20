package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.support.hook.Hook;
import ga.justreddy.wiki.reggwars.support.hook.hooks.DecentHologramsHook;
import ga.justreddy.wiki.reggwars.support.hook.hooks.LuckPermsHook;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class HookManager {

    private static HookManager manager;

    public static HookManager getManager() {
        return manager == null ? manager = new HookManager() : manager;
    }

    private final Map<String, Hook> hooks;
    private LuckPerms luckPerms;

    private HookManager() {
        this.hooks = new HashMap<>();
        ChatUtil.sendConsole("&7[&dREggWars&7] &aHooking into plugins...");
    }

    public void start() {
        register(new DecentHologramsHook());
        register(new LuckPermsHook());
    }

    private void register(Hook hook) {
        if (!hook.canHook()) {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cCould not hook into " + hook.getHookId() + " because it is not installed.");
            return;
        }
        hook.hook(REggWars.getInstance());
        hooks.put(hook.getHookId(), hook);
        ChatUtil.sendConsole("&7[&dREggWars&7] &aHooked into " + hook.getHookId());
    }

    public boolean hasHook(String hook) {
        return hooks.containsKey(hook);
    }


    public void disable() {
        for (Hook hook : hooks.values()) {
            hook.disable(REggWars.getInstance());
        }
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public void setLuckPerms(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }
}
