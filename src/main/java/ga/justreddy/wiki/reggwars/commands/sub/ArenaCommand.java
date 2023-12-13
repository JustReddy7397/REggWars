package ga.justreddy.wiki.reggwars.commands.sub;

import ga.justreddy.wiki.reggwars.REggWars;
import ga.justreddy.wiki.reggwars.api.model.entity.IGamePlayer;
import ga.justreddy.wiki.reggwars.api.model.game.GameState;
import ga.justreddy.wiki.reggwars.api.model.game.IGame;
import ga.justreddy.wiki.reggwars.api.model.game.team.IGameTeam;
import ga.justreddy.wiki.reggwars.api.model.game.team.Team;
import ga.justreddy.wiki.reggwars.commands.Command;
import ga.justreddy.wiki.reggwars.manager.GameManager;
import ga.justreddy.wiki.reggwars.model.creator.GameCreator;
import ga.justreddy.wiki.reggwars.model.entity.GamePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author JustReddy
 */
public class ArenaCommand extends Command {

    public ArenaCommand(REggWars plugin) {
        super(plugin);
        setName("arena");
        setDescription("Arena commands");
        setAliases(Collections.singletonList("game"));
        setSyntax("fuck you");
        setPermission("eggwars.command.game");
    }

    @Override
    public void onCommand(IGamePlayer gamePlayer, String[] args) {
        if (args.length < 2) {
            // TODO send help message
            return;
        }

        switch (args[1]) {
            case "create":
                createGame(gamePlayer, args); break;
            case "fstart":
                fstart(gamePlayer); break;
            case "fstop":
                fstop(gamePlayer); break;
            case "team":
                teamCommand(gamePlayer, args); break;
            case "waitinglobby":
                waitingLobby(gamePlayer, args); break;
            case "spectator":
                spectatorCommand(gamePlayer, args); break;
            case "minplayers":
                minPlayersCommand(gamePlayer, args); break;
            case "save":
                saveGameCommand(gamePlayer); break;
        }

    }

    private void saveGameCommand(IGamePlayer gamePlayer) {
        GameCreator.getCreator().save(gamePlayer);
    }

    private void minPlayersCommand(IGamePlayer gamePlayer, String[] args) {
        if (args.length < 3) {
            // TODO send proper message
            gamePlayer.sendLegacyMessage(
                    "/ew arena minplayers <amount>");
            return;
        }

        int players;
        try {
            players = Integer.parseInt(args[2]);
        }catch (NumberFormatException e) {
            gamePlayer.sendLegacyMessage("haha invalid player count@!!!");
            // TODO
            return;
        }

        GameCreator.getCreator().setMinPlayers(gamePlayer, players);

    }

    private void createGame(IGamePlayer player, String[] args) {

        if (args.length < 3) {
            // TODO send proper message
            player.sendLegacyMessage(
                    "/ew arena create <name>");
            return;
        }

        String name = args[2];

        GameCreator.getCreator().create(player, name);

    }

    private void fstart(IGamePlayer player) {
        IGame game = player.getGame();

        if (game == null) return;

        if (!game.isGameState(GameState.WAITING)) return;

        game.onGameStart();
    }

    private void fstop(IGamePlayer player) {
        IGame game = player.getGame();

        if (game == null) return;

        if (!game.isGameState(GameState.PLAYING)) return;

        game.onGameEnd(null);
    }


    private void teamCommand(IGamePlayer gamePlayer, String[] args) {
        if (args.length < 3) {
            // TODO send proper message
            gamePlayer.sendLegacyMessage(
                    "/ew arena team <create/setegg/setspawn>");
            return;
        }


        if (args[2].equalsIgnoreCase("create")) {
            if (args.length < 4) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage(
                        "/ew arena team create <team>");
                return;
            }
            Team team = Team.getByIdentifier(args[3]);
            if (team == null) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage("Invalid team!");
                return;
            }
            GameCreator.getCreator().addTeam(gamePlayer, team);
        } else if (args[2].equalsIgnoreCase("setegg")) {
            if (args.length < 4) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage(
                        "/ew arena team setegg <team>");
                return;
            }
            Team team = Team.getByIdentifier(args[3]);
            if (team == null) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage("Invalid team!");
                return;
            }
            GameCreator.getCreator().setEgg(gamePlayer, team);
        } else if (args[2].equalsIgnoreCase("setspawn")) {
            if (args.length < 4) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage(
                        "/ew arena team setspawn <team>");
                return;
            }
            Team team = Team.getByIdentifier(args[3]);
            if (team == null) {
                // TODO send proper message
                gamePlayer.sendLegacyMessage("Invalid team!");
                return;
            }
            GameCreator.getCreator().setSpawn(gamePlayer, team);
        } else {
            // TODO send proper message
            gamePlayer.sendLegacyMessage(
                    "/ew arena team <create/setegg/setspawn>");
        }


    }

    public void waitingLobby(IGamePlayer player, String[] args) {
        GameCreator.getCreator().setWaitingLobby(player);
    }

    public void spectatorCommand(IGamePlayer player, String[] args) {
        GameCreator.getCreator().setSpectatorSpawn(player);
    }





}
