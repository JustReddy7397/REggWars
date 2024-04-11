package ga.justreddy.wiki.reggwars.api.model.game.shop;

/**
 * @author JustReddy
 */
public enum ShopType {

    NORMAL("NORMAL"),
    UPGRADE("UPGRADE");

    private final String id;

    ShopType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ShopType getById(String id) {
        for (ShopType type : values()) {
            if (type.getId().equalsIgnoreCase(id)) {
                return type;
            }
        }
        return null;
    }

}
