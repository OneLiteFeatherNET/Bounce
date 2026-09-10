package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

/**
 * Called when a player lands on a death block (e.g. a redstone block placed by the area filler).
 *
 * @version 1.0.0
 * @since 0.1.0
 * @author theEvilReaper
 */
@SuppressWarnings("java:S6206")
public final class PlayerDeathBlockEvent implements PlayerEvent {

    private final Player player;

    /**
     * Constructs a new PlayerDeathBlockEvent for the specified player.
     *
     * @param player the player who landed on the death block
     */
    public PlayerDeathBlockEvent(Player player) {
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
