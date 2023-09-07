package ga.justreddy.wiki.reggwars.packets;

/**
 * @author JustReddy
 */
public enum TeamData {

    v1_8("g", "c", "d", "a", "h", "i", "b"),
    v1_9("h", "c", "d", "a", "i", "j", "b"),
    v1_10("h", "c", "d", "a", "i", "j", "b"),
    v1_11("h", "c", "d", "a", "i", "j", "b"),
    v1_12("h", "c", "d", "a", "i", "j", "b"),
    v1_14("h", "c", "d", "a", "i", "j", "b"),
    v1_15("h", "c", "d", "a", "i", "j", "b"),
    v1_16("h", "c", "d", "a", "i", "j", "b"),
    v1_17("j", "b", "c", "i", "h", "g", "a"),
    v1_18("j", "b", "c", "i", "h", "g", "a"),
    v1_19("j", "b", "c", "i", "h", "g", "a"),
    v1_20("j", "b", "c", "i", "h", "g", "a");

    private final String members;
    private final String prefix;
    private final String suffix;
    private final String teamName;
    private final String paramInt;
    private final String packOption;
    private final String displayName;

    TeamData(String members, String prefix, String suffix, String teamName, String paramInt, String packOption, String displayName) {
        this.members = members;
        this.prefix = prefix;
        this.suffix = suffix;
        this.teamName = teamName;
        this.paramInt = paramInt;
        this.packOption = packOption;
        this.displayName = displayName;
    }

    public String getMembers() {
        return members;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getParamInt() {
        return paramInt;
    }

    public String getPackOption() {
        return packOption;
    }

    public String getDisplayName() {
        return displayName;
    }


}
