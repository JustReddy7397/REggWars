package ga.justreddy.wiki.reggwars.api;

/**
 * @author JustReddy
 */
public class EggWarsProvider {

    private static EggWarsAPI api;

    public static EggWarsAPI getApi() {
        return api;
    }

    public static void setApi(EggWarsAPI api) {
        EggWarsProvider.api = api;
    }
}
