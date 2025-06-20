package net.theevilreaper.bounce.event;

import net.theevilreaper.xerus.api.event.GameFinishEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link BounceGameFinishEvent} extends the {@link GameFinishEvent} to provide specific reasons for finishing a Bounce game.
 * It includes reasons such as time over, one player left, or a player left the game.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public class BounceGameFinishEvent extends GameFinishEvent<BounceGameFinishEvent.Reason> {

    /**
     * Constructs a new BounceGameFinishEvent with the specified reason.
     *
     * @param reason the reason for finishing the game
     */
    public BounceGameFinishEvent(@NotNull Reason reason) {
        super(reason);
    }

    /**
     * Enum representing the reasons for finishing a Bounce game.
     */
    public enum Reason {

        TIME_OVER,
        ONE_PLAYER_LEFT,
        PLAYER_LEFT,
    }
}
