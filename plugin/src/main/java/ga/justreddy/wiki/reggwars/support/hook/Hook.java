package ga.justreddy.wiki.reggwars.support.hook;

import ga.justreddy.wiki.reggwars.REggWars;

/**
 * @author JustReddy
 */
public interface Hook {

    String getHookId();

    boolean canHook();

    void hook(REggWars plugin);

    void disable(REggWars plugin);

}
