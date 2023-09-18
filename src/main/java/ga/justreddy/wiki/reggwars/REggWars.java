package ga.justreddy.wiki.reggwars;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.commands.BaseCommand;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.exceptions.DependencyNotInstalledException;
import ga.justreddy.wiki.reggwars.listener.GameListener;
import ga.justreddy.wiki.reggwars.listener.MainListener;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.manager.LanguageManager;
import ga.justreddy.wiki.reggwars.manager.MapManager;
import ga.justreddy.wiki.reggwars.manager.MenuManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.game.map.FlatAdapter;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeAdapter;
import ga.justreddy.wiki.reggwars.model.replays.ReplayAdapter;
import ga.justreddy.wiki.reggwars.model.replays.advancedreplay.AdvancedReplayAdapter;
import ga.justreddy.wiki.reggwars.nms.Nms;
import ga.justreddy.wiki.reggwars.schematic.ISchematic;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.player.PlayerUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class REggWars extends JavaPlugin {

    @Getter
    private static REggWars instance;

    private boolean isSlimeEnabled;
    private boolean isReplayEnabled;
    private boolean isPlaceholderAPIEnabled;

    private SlimePlugin slime;
    private SlimeLoader loader;

    // Adapters
    private ResetAdapter resetAdapter;
    private ReplayAdapter replayAdapter;

    // Version specific stuff
    private Nms nms;
    private ISchematic schematic;

    private Config generatorsConfig;
    private Config settingsConfig;

    private BaseCommand command;

    private static final String VERSION = getVersion(Bukkit.getServer());


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if (!loadConfig()) return;
        ChatUtil.sendConsole("&7[&dREggWars&7] &aFinding NMS version...");
        try {
            nms = (Nms) Class.forName("ga.justreddy.wiki.reggwars.nms." + VERSION + "." + VERSION).newInstance();
            ChatUtil.sendConsole("&7[&dREggWars&7] &aNMS version found: " + VERSION);
        } catch (Exception e) {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cVersion: " + VERSION + " not supported! Shutting down...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ChatUtil.sendConsole("&7[&dREggWars&7] &aFinding Schematic version...");
        try {
            schematic = (ISchematic)
                    Class.forName("ga.justreddy.wiki.reggwars.nms."
                            + VERSION + "."
                            + "Schematic").newInstance();
            ChatUtil.sendConsole("&7[&dREggWars&7] &aSchematic version found: " + VERSION);
        }catch (Exception e) {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cSchematic version: " + VERSION + " not supported! Shutting down...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }




        isSlimeEnabled = settingsConfig.getConfig().getBoolean("modules.slimeworldmanager");
        if (isSlimeEnabled) {
            slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
            if (slime == null) {
                Bukkit.getPluginManager().disablePlugin(this);
                throw new DependencyNotInstalledException("SlimeWorldManager");
            }
            loader = slime.getLoader("file");
            resetAdapter = new SlimeAdapter();
            System.out.println("boom");
        } else {
            resetAdapter = new FlatAdapter();
        }

        isReplayEnabled = settingsConfig.getConfig().getBoolean("modules.advancedreplay");
        if (isReplayEnabled) {
            replayAdapter = new AdvancedReplayAdapter();
            Bukkit.getPluginManager().registerEvents(replayAdapter, this);
        }


        getCommand("eggwars").setExecutor(command = new BaseCommand(this));

        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new MainListener(), this);

        registerManagers();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @SneakyThrows
    private boolean loadConfig() {
        settingsConfig = new Config("settings.yml");
        generatorsConfig = new Config("generators.yml");
        return true;
    }

    private void registerManagers() {
        MapManager.getManager();
        LanguageManager.getManager().start();
        MenuManager.getManager().start();
        GameManager.getManager().start();
        DanceManager.getManager().start();
        KillMessageManager.getManager().start();
    }

    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }


}
