package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link ScoreDeathUpdateEvent} is triggered when a player dies and their score needs to be updated.
 * This event is used to handle the logic related to updating the player's score upon death.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public class ScoreDeathUpdateEvent implements PlayerEvent {

    private final Player player;

    /**
     * Constructs a new ScoreDeathUpdateEvent for the specified player.
     *
     * @param player the player whose score is being updated upon death
     */
    public ScoreDeathUpdateEvent(@NotNull Player player) {
        this.player = player;
    }

    /**
     * Gets the player associated with this event.
     *
     * @return the player whose score is being updated
     */
    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
