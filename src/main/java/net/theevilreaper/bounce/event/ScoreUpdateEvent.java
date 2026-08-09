package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

/**
 * The {@link ScoreUpdateEvent} is triggered when a player's score is updated.
 * This event is used to handle the logic related to updating the player's score.
 *
 * @author theEvilReaper
 * @version 1.0.1
 * @since 0.1.0
 */
public class ScoreUpdateEvent implements PlayerEvent {

    private final Player player;
    private final int points;

    /**
     * Constructs a new ScoreUpdateEvent for the specified player and points.
     *
     * @param player the player whose score is being updated
     * @param points the number of points to be added to the player's score
     */
    public ScoreUpdateEvent(Player player, int points) {
        this.player = player;
        this.points = points;
    }

    /**
     * Gets the number of points added to the player's score.
     *
     * @return the points added to the player's score
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the player associated with this event.
     *
     * @return the player whose score is being updated
     */
    @Override
    public Player getPlayer() {
        return player;
    }
}
