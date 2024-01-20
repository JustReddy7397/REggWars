package ga.justreddy.wiki.reggwars.manager;

import ga.justreddy.wiki.reggwars.api.model.quests.Quest;
import ga.justreddy.wiki.reggwars.api.model.quests.QuestType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JustReddy
 */
public class QuestManager {

    private static QuestManager manager;

    public static QuestManager getManager() {
        return manager == null ? manager = new QuestManager() : manager;
    }

    private final Map<QuestType, Quest> quests;

    public QuestManager() {
        this.quests = new HashMap<>();
    }

    public void start() {

    }

    private void register(Quest quest) {
        quests.put(quest.getQuestType(), quest);
    }

    public Quest getQuest(QuestType questType) {
        return quests.get(questType);
    }

}
