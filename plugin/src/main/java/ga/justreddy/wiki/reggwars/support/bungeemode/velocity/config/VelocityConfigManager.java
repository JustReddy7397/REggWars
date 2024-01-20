package ga.justreddy.wiki.reggwars.support.bungeemode.velocity.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VelocityConfigManager {

    static File folder;
    @Getter
    static VelocityConfig config;
    @Getter static List<VelocityConfig> configs;

    public static void initialize(File folder) {
        config = new VelocityConfig(folder, "velocity-config.yml");
        configs = new ArrayList<>();
        add(config);
    }

    public static void reload() {
        configs.forEach(config -> {
            config.reload();
            config.initiate(folder);
        });
    }

    public static void add(VelocityConfig config) {
        configs.add(config);
    }

}
