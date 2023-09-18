package ga.justreddy.wiki.reggwars.model.cosmetics.messages;

import ga.justreddy.wiki.reggwars.api.model.cosmetics.CosmeticRarity;
import ga.justreddy.wiki.reggwars.api.model.cosmetics.KillMessage;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.Team;

/**
 * @author JustReddy
 */
public class DefaultMessage extends KillMessage {

    public DefaultMessage() {
        super(0, "Default", 0, "eggwars.cosmetics.messages.default", CosmeticRarity.COMMON);
    }

    @Override
    public void sendProjectileMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal) {
        Team vicTeam = victim.getTeam().getTeam();
        String vicColor = vicTeam.getDisplayName().substring(0, 2);
        Team killTeam = killer.getTeam().getTeam();
        String killColor = killTeam.getDisplayName().substring(0, 2);
        StringBuilder builder = new StringBuilder();
        builder.append(vicColor)
                .append(victim.getName())
                .append(" &7got shot by ")
                .append(killColor)
                .append(killer.getName());
        game.sendLegacyMessage(builder.toString());
    }

    @Override
    public void sendMeleeMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal) {
        Team vicTeam = victim.getTeam().getTeam();
        String vicColor = vicTeam.getDisplayName().substring(0, 2);
        Team killTeam = killer.getTeam().getTeam();
        String killColor = killTeam.getDisplayName().substring(0, 2);
        StringBuilder builder = new StringBuilder();
        builder.append(vicColor)
                .append(victim.getName())
                .append(" &7got killed by ")
                .append(killColor)
                .append(killer.getName());
        game.sendLegacyMessage(builder.toString());
    }

    @Override
    public void sendVoidMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal) {
        Team vicTeam = victim.getTeam().getTeam();
        String vicColor = vicTeam.getDisplayName().substring(0, 2);
        Team killTeam = killer.getTeam().getTeam();
        String killColor = killTeam.getDisplayName().substring(0, 2);
        StringBuilder builder = new StringBuilder();
        builder.append(vicColor)
                .append(victim.getName())
                .append(" &7got knocked into the void by ")
                .append(killColor)
                .append(killer.getName());
        game.sendLegacyMessage(builder.toString());
    }

    @Override
    public void sendFallMessage(IGame game, IGamePlayer killer, IGamePlayer victim, boolean isFinal) {
        Team vicTeam = victim.getTeam().getTeam();
        String vicColor = vicTeam.getDisplayName().substring(0, 2);
        Team killTeam = killer.getTeam().getTeam();
        String killColor = killTeam.getDisplayName().substring(0, 2);
        StringBuilder builder = new StringBuilder();
        builder.append(vicColor)
                .append(victim.getName())
                .append(" &7fell to their death by ")
                .append(killColor)
                .append(killer.getName());
        game.sendLegacyMessage(builder.toString());
    }

    @Override
    public void sendEggBreakMessage(IGame game, IGamePlayer breaker, IGameTeam team) {
        Team vicTeam = team.getTeam();
        Team killTeam = breaker.getTeam().getTeam();
        String killColor = killTeam.getDisplayName().substring(0, 2);
        StringBuilder builder = new StringBuilder();
        builder.append(vicTeam.getBold())
                .append(" &7their egg got broken by ")
                .append(killColor)
                .append(breaker.getName());
        game.sendLegacyMessage(builder.toString());
    }
}
