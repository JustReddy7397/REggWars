package ga.justreddy.wiki.reggwars.api.model.entity;

/**
 * @author JustReddy
 */
public interface ICombatLog {

    IGamePlayer getAttacker();

    IGamePlayer getTarget();

    boolean hasTarget();

    void setTarget(IGamePlayer gamePlayer);

    boolean isTimeCorrect();

}
