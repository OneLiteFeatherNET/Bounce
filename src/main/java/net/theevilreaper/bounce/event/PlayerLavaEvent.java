package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

/**
 * Called when a player falls into lava.
 *
 * @version 1.0.0
 * @since 0.1.0
 * @author theEvilReaper
 */
@SuppressWarnings("java:S6206")
public final class PlayerLavaEvent implements PlayerEvent {

    private final Player player;

    /**
     * Constructs a new PlayerLavaEvent for the specified player.
     *
     * @param player the player who is in lava
     */
    public PlayerLavaEvent(Player player) {
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        return this.player;
    }
}
