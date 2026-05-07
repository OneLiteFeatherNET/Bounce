package net.theevilreaper.bounce.common.config;

/**
 * Concrete implementation of the {@link GameConfig.Builder} interface.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameConfigBuilder implements GameConfig.Builder {

    private int minPlayers;
    private int maxPlayers;
    private int lobbyTime;
    private int maxGameTime;

    GameConfigBuilder() {
        // Hide the public constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameConfig.Builder minPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameConfig.Builder maxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameConfig.Builder lobbyTime(int lobbyTime) {
        if (lobbyTime <= GameConfig.FORCE_START_TIME) {
            throw new IllegalArgumentException("Lobby time must be greater than " + GameConfig.FORCE_START_TIME);
        }
        this.lobbyTime = lobbyTime;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameConfig.Builder gameTime(int gameTime) {
        this.maxGameTime = gameTime;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameConfig build() {
        return new GameConfigImpl(minPlayers, maxPlayers, lobbyTime, maxGameTime);
    }
}
