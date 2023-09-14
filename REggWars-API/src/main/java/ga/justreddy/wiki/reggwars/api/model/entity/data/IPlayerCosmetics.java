package ga.justreddy.wiki.reggwars.api.model.entity.data;

import java.util.List;

/**
 * @author JustReddy
 */
public interface IPlayerCosmetics {

    int getSelectedCage();

    void setSelectedCage(int id);

    List<Integer> getCages();

    void addCage(int id);

    boolean hasCage(int id);

    void removeCage(int id);

    int getSelectedDance();

    void setSelectedDance(int id);

    List<Integer> getDances();

    void addDance(int id);

    boolean hasDance(int id);

    void removeDance(int id);

    int getSelectedKillEffect();

    void setSelectedKillEffect(int id);

    List<Integer> getKillEffects();

    void addKillEffect(int id);

    boolean hasKillEffect(int id);

    void removeKillEffect(int id);

    int getSelectedTrail();

    void setSelectedTrail(int id);

    List<Integer> getTrails();

    int getSelectedKillMessage();

    void setSelectedKillMessage(int id);

    void addKillMessage(int id);

    boolean hasKillMessage(int id);

    void removeKillMessage(int id);

    List<Integer> getKillMessages();

    void addTrail(int id);

    boolean hasTrail(int id);

    void removeTrail(int id);

    int getSelectedDeathCry();

    void setSelectedDeathCry(int id);

    void addDeathCry(int id);

    boolean hasDeathCry(int id);

    void removeDeathCry(int id);

    List<Integer> getDeathCries();


}
