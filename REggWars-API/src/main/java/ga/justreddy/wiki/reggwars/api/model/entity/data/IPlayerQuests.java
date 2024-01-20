package ga.justreddy.wiki.reggwars.api.model.entity.data;

import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.quests.Quest;
import ga.justreddy.wiki.reggwars.api.model.quests.QuestType;

import java.util.List;
import java.util.Map;

/**
 * @author JustReddy
 */
public interface IPlayerQuests {

    Map<QuestType, Long> getCompletedQuests();

    List<QuestType> getActiveQuests();

    Map<QuestType, Integer> getQuestProgress();

    void start(QuestType questType);

    void complete(QuestType questType);

    void remove(QuestType questType);

    void update(IGamePlayer gamePlayer, QuestType type);

    boolean isQuestCompleted(QuestType questType);

    boolean isQuestActive(QuestType questType);

}
