package ga.justreddy.wiki.reggwars.model.game.map;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author JustReddy
 */
@RequiredArgsConstructor
@Data
public class SlimeWorldHasher implements Serializable {

    private final String hashCode;
    private final String name;
    private final byte[] data;
    private final String type;


}
