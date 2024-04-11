package ga.justreddy.wiki.reggwars.utils.world;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * @author JustReddy
 */
@RequiredArgsConstructor
@Data
public class BukkitWorldHasher implements Serializable {

    private final String name;
    private final File file;
    private final String type;


}
