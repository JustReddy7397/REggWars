package ga.justreddy.wiki.reggwars.api.model.quests;

/**
 * @author JustReddy
 */
public enum CooldownType {

    DAILY("1d"),
    WEEKLY("1w");

    private final String cooldown;

    CooldownType(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getCooldown() {
        return cooldown;
    }


}
