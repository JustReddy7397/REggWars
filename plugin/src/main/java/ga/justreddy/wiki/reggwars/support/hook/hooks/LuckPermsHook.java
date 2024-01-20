package ga.justreddy.wiki.reggwars.support.hook.hooks;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.VictoryDance;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerCosmetics;
import ga.justreddy.wiki.reggwars.manager.HookManager;
import ga.justreddy.wiki.reggwars.manager.PlayerManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.entity.data.PlayerCosmetics;
import ga.justreddy.wiki.reggwars.support.hook.Hook;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedData;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.UserLoadEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.util.UUID;

/**
 * @author JustReddy
 */
public class LuckPermsHook implements Hook {

    private EventSubscription<?> addEvent;
    private EventSubscription<?> removeEvent;
    private EventSubscription<?> loadEvent;

    @Override
    public String getHookId() {
        return "LuckPerms";
    }

    @Override
    public boolean canHook() {
        return Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
    }

    @Override
    public void hook(REggWars plugin) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        HookManager.getManager().setLuckPerms(luckPerms);
        EventBus bus = luckPerms.getEventBus();
        addEvent = bus.subscribe(plugin, NodeAddEvent.class, event -> onNodeAdd(plugin, event));
        removeEvent = bus.subscribe(plugin, NodeRemoveEvent.class, event -> onNodeRemove(plugin, event));
        loadEvent = bus.subscribe(plugin, UserLoadEvent.class, event -> onUserLoad(plugin, event));
    }

    @Override
    public void disable(REggWars plugin) {
        addEvent.close();
        removeEvent.close();
        loadEvent.close();
    }

    private void onNodeAdd(Plugin plugin, NodeAddEvent event) {
        if (!event.isUser()) return;

        User target = (User) event.getTarget();
        Node node = event.getNode();

        if (!(node instanceof PermissionNode)) return;

        String permission = ((PermissionNode) node).getPermission();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            final Player player = Bukkit.getPlayer(target.getUniqueId());
            if (player == null) return;
            final IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
            if (gamePlayer == null) return;
            IPlayerCosmetics cosmetics = gamePlayer.getCosmetics();
            if (permission.startsWith("*")) {
                // TODO DO ALL COSMETICS
                for (VictoryDance dance : DanceManager.getManager().getDances().values()) {
                    cosmetics.addDance(dance.getId());
                }
                return;
            }

            // TODO DO ALL COSMETICS 2
            if (permission.startsWith("eggwars.cosmetics.dances")) {
                VictoryDance dance = DanceManager.getManager().getByPermission(permission);
                if (dance == null) return;
                cosmetics.addDance(dance.getId());
            }

            if (permission.startsWith("eggwars.cosmetics.messages")) {
                KillMessage message = KillMessageManager.getManager().getByPermission(permission);
                if (message == null) return;
                cosmetics.addKillMessage(message.getId());
            }

        });

    }

    private void onNodeRemove(Plugin plugin, NodeRemoveEvent event) {
        if (!event.isUser()) return;

        User target = (User) event.getTarget();
        Node node = event.getNode();

        if (!(node instanceof PermissionNode)) return;

        String permission = ((PermissionNode) node).getPermission();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            final Player player = Bukkit.getPlayer(target.getUniqueId());
            if (player == null) return;
            final IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(player.getUniqueId());
            if (gamePlayer == null) return;
            IPlayerCosmetics cosmetics = gamePlayer.getCosmetics();
            if (permission.startsWith("*")) {
                // TODO DO ALL COSMETICS
                for (VictoryDance dance : DanceManager.getManager().getDances().values()) {
                    cosmetics.removeDance(dance.getId());
                }

                for (KillMessage killMessage : KillMessageManager.getManager().getMessages().values()) {
                    cosmetics.removeKillMessage(killMessage.getId());
                }

                return;
            }

            // TODO DO ALL COSMETICS 2
            if (permission.startsWith("eggwars.cosmetics.dances")) {
                VictoryDance dance = DanceManager.getManager().getByPermission(permission);
                if (dance == null) return;
                cosmetics.removeDance(dance.getId());
            }

            if (permission.startsWith("eggwars.cosmetics.messages")) {
                KillMessage message = KillMessageManager.getManager().getByPermission(permission);
                if (message == null) return;
                cosmetics.removeKillMessage(message.getId());
            }
        });
    }



    private void onUserLoad(Plugin plugin, UserLoadEvent event) {
        User user = event.getUser();
        UUID uuid = user.getUniqueId();
        CachedDataManager dataManager = user.getCachedData();
        CachedPermissionData permissionData = dataManager.getPermissionData();
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            IGamePlayer gamePlayer = PlayerManager.getManager().getGamePlayer(uuid);
            if (gamePlayer == null) return;
            IPlayerCosmetics cosmetics = gamePlayer.getCosmetics();
            if (hasPermission(user, "*")) {
                // TODO DO ALL COSMETICS
                for (VictoryDance dance : DanceManager.getManager().getDances().values()) {
                    cosmetics.addDance(dance.getId());
                }
            }

            for (VictoryDance dance : DanceManager.getManager().getDances().values()) {
                if (hasPermission(user, dance.getPermission())) {
                    cosmetics.addDance(dance.getId());
                }
            }

            for (KillMessage killMessage : KillMessageManager.getManager().getMessages().values()) {
                if (hasPermission(user, killMessage.getPermission())) {
                    cosmetics.addKillMessage(killMessage.getId());
                }
            }





        }, 100L);

    }

    private boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

}
