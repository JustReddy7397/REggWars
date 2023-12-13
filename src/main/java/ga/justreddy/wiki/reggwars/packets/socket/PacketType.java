package ga.justreddy.wiki.reggwars.packets.socket;

import java.io.Serializable;

/**
 * @author JustReddy
 */
public enum PacketType implements Serializable {
    GAMES_ADD, GAME_JOIN, GAME_LEAVE, GAME_REMOVE,
    GAME_UPDATE, BACK_TO_SERVER,
    FORCE_START, FORCE_STOP, KICK,
    SERVER_GAMES_ADD, SERVER_GAMES_REMOVE,
    GAMES_REQUEST, GAMES_SEND,
    CONFIG_UPDATE

}
