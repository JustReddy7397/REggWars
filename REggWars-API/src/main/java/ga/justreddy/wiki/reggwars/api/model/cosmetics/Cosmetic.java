package ga.justreddy.wiki.reggwars.api.model.cosmetics;

/**
 * @author JustReddy
 */
public abstract class Cosmetic {

    private final int id;
    private final String subname;
    private final int cost;
    private final String permission;
    private final CosmeticRarity rarity;

    public Cosmetic(int id, String subname, int cost, String permission, CosmeticRarity rarity) {
        this.id = id;
        this.subname = subname;
        this.cost = cost;
        this.permission = permission;
        this.rarity = rarity;
    }

    public int getId() {
        return id;
    }

    public String getSubname() {
        return subname;
    }

    public int getCost() {
        return cost;
    }

    public String getPermission() {
        return permission;
    }

    public CosmeticRarity getRarity() {
        return rarity;
    }
}
