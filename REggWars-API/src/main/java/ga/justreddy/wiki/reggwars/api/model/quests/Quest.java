package ga.justreddy.wiki.reggwars.api.model.quests;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;

import java.util.*;

/**
 * @author JustReddy
 */

public abstract class Quest {


    private String name;
    private String[] description;
    private int requiredAmount;
    private CooldownType cooldownType;
    private QuestType questType;

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return Arrays.asList(description);
    }

    protected void setDescription(String[] description) {
        this.description = description;
    }

    protected void setDescription(List<String> description) {
        this.description = description.toArray(new String[0]);
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    protected void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public CooldownType getCooldownType() {
        return cooldownType;
    }

    protected void setCooldownType(CooldownType cooldownType) {
        this.cooldownType = cooldownType;
    }

    public QuestType getQuestType() {
        return questType;
    }

    protected void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public void trigger(IGamePlayer player) {
        if (player.getQuests().isQuestCompleted(questType)) return;
        if (!player.getQuests().isQuestActive(questType)) return;

        int progress = player.getQuests().getQuestProgress().getOrDefault(questType, 0);
        progress++;
        player.getQuests().getQuestProgress().put(questType, progress);

        if (progress >= requiredAmount) {
            complete(player);
        }

    }

    public void complete(IGamePlayer player) {
        player.getQuests().complete(questType);
    }


}
