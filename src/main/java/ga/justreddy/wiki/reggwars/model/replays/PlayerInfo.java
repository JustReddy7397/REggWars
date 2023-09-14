package ga.justreddy.wiki.reggwars.model.replays;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Data
public class PlayerInfo {

    String name;
    UUID uuid;
    String tabPrefix;
    String tabSuffix;
    String tagPrefix;
    String tagSuffix;

}
