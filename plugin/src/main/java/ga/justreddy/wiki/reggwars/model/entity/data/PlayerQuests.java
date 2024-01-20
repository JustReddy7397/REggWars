package ga.justreddy.wiki.reggwars.model.entity.data;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.entity.data.IPlayerQuests;
import ga.justreddy.wiki.reggwars.api.model.quests.Quest;
import ga.justreddy.wiki.reggwars.api.model.quests.QuestType;
import ga.justreddy.wiki.reggwars.manager.QuestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public class PlayerQuests implements IPlayerQuests {

    private final Map<QuestType, Long> completedQuests;
    private final List<QuestType> activeQuests;
    private final Map<QuestType, Integer> questProgress;

    public PlayerQuests() {
        this.completedQuests = new HashMap<>();
        this.activeQuests = new ArrayList<>();
        this.questProgress = new HashMap<>();
    }

    @Override
    public Map<QuestType, Long> getCompletedQuests() {
        return completedQuests;
    }

    @Override
    public List<QuestType> getActiveQuests() {
        return activeQuests;
    }

    @Override
    public Map<QuestType, Integer> getQuestProgress() {
        return questProgress;
    }

    @Override
    public void start(QuestType questType) {
        if (isQuestCompleted(questType)) return;
        if (isQuestActive(questType)) return;
        activeQuests.add(questType);
    }

    @Override
    public void complete(QuestType questType) {
        completedQuests.put(questType, System.currentTimeMillis()); // TODO: 2021-10-10 add time
        activeQuests.remove(questType);
    }

    @Override
    public void remove(QuestType questType) {
        completedQuests.remove(questType);
        questProgress.remove(questType);
    }

    @Override
    public void update(IGamePlayer player, QuestType type) {
        Quest quest = QuestManager.getManager().getQuest(type);
        if (quest == null) return;
        quest.trigger(player);
    }

    @Override
    public boolean isQuestCompleted(QuestType questType) {
        return completedQuests.containsKey(questType);
    }

    @Override
    public boolean isQuestActive(QuestType questType) {
        return activeQuests.contains(questType);
    }
}
