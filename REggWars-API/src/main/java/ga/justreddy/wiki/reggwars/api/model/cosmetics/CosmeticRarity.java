package ga.justreddy.wiki.reggwars.api.model.cosmetics;

/**
 * @author JustReddy
 */
public enum CosmeticRarity {

    COMMON("&7Common"),
    UNCOMMON("&aUncommon"),
    RARE("&9Rare"),
    EPIC("&5Epic"),
    LEGENDARY("&6Legendary");

    private final String display;

    CosmeticRarity(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
