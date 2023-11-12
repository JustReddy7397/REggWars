package ga.justreddy.wiki.reggwars;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.bungee.Core;
import ga.justreddy.wiki.reggwars.bungee.ServerLobbies;
import ga.justreddy.wiki.reggwars.bungee.ServerMode;
import ga.justreddy.wiki.reggwars.commands.BaseCommand;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.exceptions.DependencyNotInstalledException;
import ga.justreddy.wiki.reggwars.listener.GameListener;
import ga.justreddy.wiki.reggwars.listener.MainListener;
import ga.justreddy.wiki.reggwars.listener.bungee.BungeeListener;
import ga.justreddy.wiki.reggwars.listener.bungee.ServerListener;
import ga.justreddy.wiki.reggwars.manager.*;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.game.map.FlatAdapter;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeAdapter;
import ga.justreddy.wiki.reggwars.model.replays.ReplayAdapter;
import ga.justreddy.wiki.reggwars.model.replays.advancedreplay.AdvancedReplayAdapter;
import ga.justreddy.wiki.reggwars.nms.Nms;
import ga.justreddy.wiki.reggwars.schematic.ISchematic;
import ga.justreddy.wiki.reggwars.storage.SQLStorage;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.player.PlayerUtil;
import javafx.scene.input.MouseDragEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

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

    private Storage storage;

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

        if (Bukkit.getServer().getSpawnRadius() != 0) {
            Bukkit.getServer().setSpawnRadius(0);
        }

        ServerMode mode = null;
        try {
            Core.MODE = ServerMode.valueOf(settingsConfig.getConfig().getString("modules.mode").toUpperCase());
            mode = Core.MODE;
        }catch (Exception e) {
            e.fillInStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (mode != ServerMode.MULTI_ARENA && storage instanceof SQLStorage) {
            cancelBungee(false);
            return;
        }

        if (mode != ServerMode.MULTI_ARENA && !storage.doesBungeeFilesExist()) {
            cancelBungee(true);
            return;
        }

        if (mode != ServerMode.MULTI_ARENA && !Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord")) {
            setEnabled(false);
            ChatUtil.sendConsole("&7[&dREggWars&7] &cBungee mode is not enabled in spigot.yml!");
            return;
        }

       if (mode != ServerMode.LOBBY) {
           isSlimeEnabled = settingsConfig.getConfig().getBoolean("modules.slimeworldmanager");
           if (isSlimeEnabled) {
               slime = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
               if (slime == null) {
                   Bukkit.getPluginManager().disablePlugin(this);
                   throw new DependencyNotInstalledException("SlimeWorldManager");
               }
               loader = slime.getLoader("file");
               resetAdapter = new SlimeAdapter();
           } else {
               resetAdapter = new FlatAdapter();
           }

           isReplayEnabled = settingsConfig.getConfig().getBoolean("modules.advancedreplay");
           if (isReplayEnabled) {
               replayAdapter = new AdvancedReplayAdapter();
               Bukkit.getPluginManager().registerEvents(replayAdapter, this);
           }
           Bukkit.getPluginManager().registerEvents(new GameListener(), this);
       }

        if (mode != ServerMode.MULTI_ARENA) {
            ServerLobbies.setupLobbies();
        }

        if (mode != ServerMode.MULTI_ARENA) {
            Bukkit.getMessenger().registerIncomingPluginChannel(this, "REggWarsAPI", new BungeeListener());
            Bukkit.getServer().getPluginManager().registerEvents(new ServerListener(), this);
        }


        getCommand("eggwars").setExecutor(command = new BaseCommand(this));

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
        LanguageManager.getManager().start();
        MenuManager.getManager().start();
        if (Core.MODE != ServerMode.LOBBY) {
            MapManager.getManager();
            GameManager.getManager().start();
        }
        DanceManager.getManager().start();
        KillMessageManager.getManager().start();
    }

    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    private void cancelBungee(boolean table) {
        this.setEnabled(false);
        if (!table) {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cYou can't use SQLite for Bungee Mode! Use MySQL or MongoDB instead!");
        } else {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cUnable to find Table with Bungee Configurations!");
            ChatUtil.sendConsole("&7[&dREggWars&7] &cSetup your BungeeCord with REggWars first!");
        }
    }



}
