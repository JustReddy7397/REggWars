package ga.justreddy.wiki.reggwars;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import ga.justreddy.wiki.reggwars.api.EggWarsProvider;
import ga.justreddy.wiki.reggwars.api.model.game.ResetAdapter;
import ga.justreddy.wiki.reggwars.api.model.language.ILanguage;
import ga.justreddy.wiki.reggwars.api.model.language.Message;
import ga.justreddy.wiki.reggwars.commands.BaseCommand;
import ga.justreddy.wiki.reggwars.config.Config;
import ga.justreddy.wiki.reggwars.exceptions.DependencyNotInstalledException;
import ga.justreddy.wiki.reggwars.socket.BungeeUtils;
import ga.justreddy.wiki.reggwars.support.ApiHandler;
import ga.justreddy.wiki.reggwars.listener.GameListener;
import ga.justreddy.wiki.reggwars.listener.MainListener;
import ga.justreddy.wiki.reggwars.manager.*;
import ga.justreddy.wiki.reggwars.manager.cosmetic.DanceManager;
import ga.justreddy.wiki.reggwars.manager.cosmetic.KillMessageManager;
import ga.justreddy.wiki.reggwars.model.game.map.FlatAdapter;
import ga.justreddy.wiki.reggwars.model.game.map.SlimeAdapter;
import ga.justreddy.wiki.reggwars.nms.Nms;
import ga.justreddy.wiki.reggwars.schematic.ISchematic;
import ga.justreddy.wiki.reggwars.socket.SocketClient;
import ga.justreddy.wiki.reggwars.storage.SQLStorage;
import ga.justreddy.wiki.reggwars.storage.type.Storage;
import ga.justreddy.wiki.reggwars.support.bungeemode.messenger.IMessenger;
import ga.justreddy.wiki.reggwars.utils.ChatUtil;
import ga.justreddy.wiki.reggwars.utils.LocationUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // Version specific stuff
    private Nms nms;
    private ISchematic schematic;

    private Storage storage;

    private Config generatorsConfig;
    private Config settingsConfig;

    private BaseCommand command;

    private boolean bungee;
    private SocketClient socketClient;
    private String serverName;

    private IMessenger<?> messenger;

    private static final Logger LOGGER = Logger.getLogger("REggWars");

    private static final String VERSION = getVersion(Bukkit.getServer());

    @Getter
    private Location spawnLocation;

    @Override
    public void onLoad() {
        instance = this;
        LibraryManager.getManager().loadDependencies();
    }

    @Override
    public void onEnable() {
        if (!loadConfig()) return;
        ChatUtil.sendConsole("&7[&dREggWars&7] &aFinding NMS version...");
        try {
            nms = (Nms) Class.forName("ga.justreddy.wiki.reggwars.nms." + VERSION + "." + VERSION).newInstance();
            ChatUtil.sendConsole("&7[&dREggWars&7] &aNMS version found: " + VERSION);
            nms.setBlastProofItems();
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
        } catch (Exception e) {
            ChatUtil.sendConsole("&7[&dREggWars&7] &cSchematic version: " + VERSION + " not supported! Shutting down...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ChatUtil.sendConsole("&7[&dREggWars&7] &aFinding API...");
        EggWarsProvider.setApi(new ApiHandler());
        ChatUtil.sendConsole("&7[&dREggWars&7] &aAPI found!");

        this.spawnLocation =
                LocationUtils.getLocation(
                        getSettingsConfig().getConfig().getString("spawn")
                );


        if (Bukkit.getServer().getSpawnRadius() != 0) {
            Bukkit.getServer().setSpawnRadius(0);
        }

        ServerMode mode = null;
        try {
            Core.MODE = ServerMode.valueOf(settingsConfig.getConfig().getString("modules.mode").toUpperCase());
            mode = Core.MODE;
        } catch (Exception e) {
            e.fillInStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (mode != ServerMode.MULTI_ARENA && storage instanceof SQLStorage) {
            cancelBungee();
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
        } else {
            resetAdapter = new FlatAdapter();
        }

        Bukkit.getPluginManager().registerEvents(new GameListener(), this);

        serverName = settingsConfig.getConfig().getString("bungee.server");

        registerManagers();


        if (mode != ServerMode.MULTI_ARENA) {
            bungee = true;

            /*socketClient = new SocketClient(
                    settingsConfig.getConfig().getString("bungee.host"),
                    settingsConfig.getConfig().getInt("bungee.port"),
                    serverName
            );
            socketClient.clientSetup();*/
        }

        getCommand("eggwars").setExecutor(command = new BaseCommand(this));
        Bukkit.getPluginManager().registerEvents(new MainListener(), this);

    }

    @Override
    public void onDisable() {
        PlayerManager.getManager().getPlayers().forEach(player -> {
            storage.savePlayer(player);
            if (Core.MODE == ServerMode.BUNGEE) {
                ILanguage language = player.getSettings().getLanguage();
                String message = language.getString(Message.MESSAGES_SERVER_RESTARTED);
                getSocketClient().getSender().sendMessagePacket(player.getUniqueId(), message);
                BungeeUtils.getInstance().sendBackToServer(player);
            }
        });
        if (socketClient != null) socketClient.closeConnections();
        HookManager.getManager().disable();

    }

    @SneakyThrows
    private boolean loadConfig() {
        settingsConfig = new Config("settings.yml");
        generatorsConfig = new Config("generators.yml");
        ConfigManager.getManager().register(settingsConfig);
        ConfigManager.getManager().register(generatorsConfig);
        return true;
    }

    private void registerManagers() {
        HookManager.getManager().start();
        new ShopManager();
        ShopManager.getManager().start();
        LanguageManager.getManager().start();
        MenuManager.getManager().start();
        MapManager.getManager();
        GameManager.getManager().start();
        DanceManager.getManager().start();
        KillMessageManager.getManager().start();
    }

    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    private void cancelBungee() {
        this.setEnabled(false);
        log(Level.SEVERE, "[!] You can't use SQLite for Bungee Mode! Use MySQL or MongoDB instead!");
    }

    public void log(Level level, String message, String... args) {
        char symbol = 0;
        if (level.getName().equals("INFO")) {
            symbol = 'X';
        } else {
            symbol = '!';
        }
        LOGGER.log(level, MessageFormat.format("[{0}] [{1}] {2}", getName(), symbol, message), args);
    }

    public void reload() {
        ConfigManager.getManager().reload();
        LanguageManager.getManager().reload();
        socketClient.getSender().sendLanguagesUpdatePacket();
    }

    @SneakyThrows
    public void setSpawnLocation(Location location) {
        getSettingsConfig().getConfig().set("spawn", LocationUtils.toLocation(location));
        getSettingsConfig().save();
        this.spawnLocation = location;
    }


}
