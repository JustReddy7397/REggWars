package ga.justreddy.wiki.reggwars.velocity.config;

import ga.justreddy.wiki.reggwars.velocity.Velocity;
import ga.justreddy.wiki.reggwars.velocity.config.impl.YamlConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.File;

/**
 * @author JustReddy
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public class VelocityConfig extends YamlConfiguration {

    static VelocityConfig config;

    static String name;

    public VelocityConfig(File folder, String path) {
        super(folder, path);
        name = path;
        reload();
    }

    public void initiate(File file) {
        config = new VelocityConfig(Velocity.getInstance().getFolder().toFile(), name);
    }

}
