package ga.justreddy.wiki.reggwars.api;

/**
 * The provider for the API
 * @author JustReddy
 */
public class EggWarsProvider {

    private static EggWarsAPI api;

    /**
     * Get the API
     * @return the API
     */
    public static EggWarsAPI getApi() {
        return api;
    }

    /**
     * Set the API
     * @param api the API
     */
    public static void setApi(EggWarsAPI api) {
        EggWarsProvider.api = api;
    }
}
