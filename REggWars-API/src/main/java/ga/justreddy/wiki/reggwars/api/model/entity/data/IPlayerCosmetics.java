package ga.justreddy.wiki.reggwars.api.model.entity.data;

import java.util.List;
import java.util.Set;

/**
 * @author JustReddy
 */
public interface IPlayerCosmetics {

    int getSelectedCage();

    void setSelectedCage(int id);

    Set<Integer> getCages();

    void addCage(int id);

    boolean hasCage(int id);

    void removeCage(int id);

    int getSelectedDance();

    void setSelectedDance(int id);

    Set<Integer> getDances();

    void addDance(int id);

    boolean hasDance(int id);

    void removeDance(int id);

    int getSelectedKillEffect();

    void setSelectedKillEffect(int id);

    Set<Integer> getKillEffects();

    void addKillEffect(int id);

    boolean hasKillEffect(int id);

    void removeKillEffect(int id);

    int getSelectedKillMessage();

    void setSelectedKillMessage(int id);

    Set<Integer> getKillMessages();

    void addKillMessage(int id);

    boolean hasKillMessage(int id);

    void removeKillMessage(int id);

    int getSelectedTrail();

    void setSelectedTrail(int id);

    Set<Integer> getTrails();

    void addTrail(int id);

    boolean hasTrail(int id);

    void removeTrail(int id);

    int getSelectedDeathCry();

    void setSelectedDeathCry(int id);

    void addDeathCry(int id);

    boolean hasDeathCry(int id);

    void removeDeathCry(int id);

    Set<Integer> getDeathCries();


}
