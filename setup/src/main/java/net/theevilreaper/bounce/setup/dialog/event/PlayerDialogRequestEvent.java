package net.theevilreaper.bounce.setup.dialog.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

/**
 * Called when a {@link Player} request a dialog for the setup
 *
 * @version 1.0.0
 * @since 0.5.0
 * @author Joltras
 */
public class PlayerDialogRequestEvent implements PlayerEvent {

    private final Player player;
    private final Target target;

    /**
     * Constructs a new PlayerDialogRequestEvent instance.
     *
     * @param player the player who requested the dialog
     * @param target the target of the dialog request
     */
    public PlayerDialogRequestEvent(Player player, Target target) {
        this.player = player;
        this.target = target;
    }

    /**
     * Gets the target of the dialog request.
     *
     * @return the target of the dialog request
     */
    public Target getTarget() {
        return this.target;
    }

    /**
     * Gets the player who requested the dialog.
     *
     * @return the player who requested the dialog
     */
    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Represents the target of the dialog request.
     */
    public enum Target {
        /**
         * The target for the dialog request is the setup name.
         */
        SETUP_NAME,
        /**
         * The target for the dialog request is to define the amount of authors to set up.
         */
        SETUP_REQUEST_AUTHOR,
        /**
         * The target for the dialog request is to set up the block bounce.
         */
        SETUP_BLOCK_BOUNCE,
        /**
         * The target for the dialog request is to set up the block weight/chance.
         */
        SETUP_BLOCK_WEIGHT,
        /**
         * The target for the dialog request is to set up the reshuffle interval.
         */
        SETUP_SHUFFLE_INTERVAL,
        /**
         * The target for the dialog request is to set up the reshuffle percentage.
         */
        SETUP_RESHUFFLE_PERCENTAGE

        ;
    }
}
