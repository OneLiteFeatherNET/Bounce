package net.theevilreaper.bounce.attribute;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;

/**
 * The {@link AttributeHelper} is a utility class to manage player attributes related to jumping.
 *
 * @author theEvilReaper
 * @version 1.0.1
 * @since 0.1.0
 */
public final class AttributeHelper {

    private static final int ZERO_JUMP = 0;
    private static final double DEFAULT_JUMP_HEIGHT = 0.41999998688697815;

    /**
     * Disables the jump strength of the player by setting it to zero.
     *
     * @param player the player whose jump strength is to be disabled
     */
    public static void disableJumpStrength(Player player) {
        player.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(ZERO_JUMP);
    }

    /**
     * Resets the jump strength of the player to the default value.
     *
     * @param player the player whose jump strength is to be reset
     */
    public static void resetJumpStrength(Player player) {
        player.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(DEFAULT_JUMP_HEIGHT);
    }

    private AttributeHelper() {
        // Nothing to do here
    }
}
