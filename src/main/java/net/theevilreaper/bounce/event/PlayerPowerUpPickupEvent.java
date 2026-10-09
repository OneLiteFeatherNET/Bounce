package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.theevilreaper.bounce.powerup.PowerUpType;

/**
 * Called when a player picks up a floating power-up.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author theEvilReaper
 */
@SuppressWarnings("java:S6206")
public final class PlayerPowerUpPickupEvent implements PlayerEvent {

    private final Player player;
    private final PowerUpType type;

    /**
     * Constructs a new PlayerPowerUpPickupEvent for the specified player.
     *
     * @param player the player who picked up the power-up
     * @param type   the type of the power-up
     */
    public PlayerPowerUpPickupEvent(Player player, PowerUpType type) {
        this.player = player;
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the type of the picked up power-up.
     *
     * @return the type
     */
    public PowerUpType getType() {
        return this.type;
    }
}
