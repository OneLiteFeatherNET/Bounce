package net.theevilreaper.bounce.common.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GameConfig} interface represents the structure for a configuration which is used by the game.
 * It contains some values which can be adjusted to change specific settings for the game.
 * There are also some static values in the interface which are also used in the game.
 * Each static value indicates that it is a constant value and should not be changed.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public sealed interface GameConfig permits GameConfigImpl, InternalGameConfig {

    String MAP_FILE = "map.json";

    int FORCE_START_TIME = 11;

    /**
     * Creates a new {@link Builder} which can be used to create a new game configuration.
     *
     * @return the builder instance
     */
    @Contract(pure = true)
    static @NotNull Builder builder() {
        return new GameConfigBuilder();
    }

    /**
     * Returns the minimum number of players required to start a game.
     *
     * @return the minimum number of players
     */
    int minPlayers();

    /**
     * Returns the maximum number of players allowed in the game.
     *
     * @return the maximum number of players
     */
    int maxPlayers();

    /**
     * Returns the lobby time in seconds.
     *
     * @return the lobby time
     */
    int lobbyTime();

    /**
     * Returns the maximum game time in seconds.
     *
     * @return the maximum game time
     */
    int gameTime();

    /**
     * Returns the size for each team.
     *
     * @return the given size
     */
    int teamSize();

    /**
     * Returns the maximum rounds for the game.
     *
     * @return the maximum rounds
     */
    int maxRounds();

    /**
     * The {@link Builder} interface is used to create a new game configuration.
     * It provides methods to set the values for the configuration.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.0.0
     */
    sealed interface Builder permits GameConfigBuilder {

        /**
         * Sets the minimum number of players required to start a game.
         *
         * @param minPlayers the minimum number of players
         * @return the builder instance
         */
        @NotNull Builder minPlayers(int minPlayers);

        /**
         * Sets the maximum number of players allowed in the game.
         *
         * @param maxPlayers the maximum number of players
         * @return the builder instance
         */
        @NotNull Builder maxPlayers(int maxPlayers);

        /**
         * Sets the lobby time in seconds.
         *
         * @param lobbyTime the lobby time
         * @return the builder instance
         * @throws IllegalArgumentException if the lobby time is than the {@link GameConfig#FORCE_START_TIME}
         */
        @NotNull Builder lobbyTime(int lobbyTime);

        /**
         * Sets the maximum game time in seconds.
         *
         * @param gameTime the maximum game time
         * @return the builder instance
         */
        @NotNull Builder gameTime(int gameTime);

        /**
         * Sets the general size for each team.
         *
         * @param teamSize the size of the team
         * @return the builder instance
         */
        @NotNull Builder teamSize(int teamSize);

        /**
         * Sets the maximum rounds for the game.
         *
         * @param maxRounds the maximum rounds
         * @return the builder instance
         */
        @NotNull Builder maxRounds(int maxRounds);

        /**
         * Builds the game configuration.
         *
         * @return the created configuration
         */
        @NotNull GameConfig build();
    }

}
