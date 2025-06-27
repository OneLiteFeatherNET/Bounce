package net.theevilreaper.bounce.common.config;

/**
 * The {@link GameConfigImpl} is the implementation of the {@link GameConfig} interface.
 * It represents a configuration which is used to adjust some settings for the game.
 * The configuration is immutable and can't be changed after the creation.
 *
 * @param minPlayers the minimum number of players required to start a game
 * @param maxPlayers the maximum number of players allowed in the game
 * @param lobbyTime  the time in seconds before the game starts
 * @param gameTime   the maximum duration of a game in minutes
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public record GameConfigImpl(
        int minPlayers,
        int maxPlayers,
        int lobbyTime,
        int gameTime
) implements GameConfig {

}
