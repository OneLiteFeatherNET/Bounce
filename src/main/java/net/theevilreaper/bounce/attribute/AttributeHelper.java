package net.theevilreaper.bounce.attribute;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;

/**
 * The {@link AttributeHelper} is a utility class to manage player attributes related to movement.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
public final class AttributeHelper {

    // Multiplies the total jump strength by zero, which keeps the base value untouched
    private static final AttributeModifier DISABLE_JUMP_MODIFIER =
            new AttributeModifier("bounce:disable_jump", -1, AttributeOperation.ADD_MULTIPLIED_TOTAL);
    // Turns the default gravity of 0.08 into -0.02, which lets the player slowly float upwards
    private static final AttributeModifier GRAVITY_FLIP_MODIFIER =
            new AttributeModifier("bounce:gravity_flip", -0.1, AttributeOperation.ADD_VALUE);

    /**
     * Disables the jump strength of the player by reducing it to zero.
     *
     * @param player the player whose jump strength is to be disabled
     */
    public static void disableJumpStrength(Player player) {
        player.getAttribute(Attribute.JUMP_STRENGTH).addModifier(DISABLE_JUMP_MODIFIER);
    }

    /**
     * Resets the jump strength of the player by removing a previously applied jump block.
     *
     * @param player the player whose jump strength is to be reset
     */
    public static void resetJumpStrength(Player player) {
        player.getAttribute(Attribute.JUMP_STRENGTH).removeModifier(DISABLE_JUMP_MODIFIER.id());
    }

    /**
     * Reverses the gravity of the player, which lets the player slowly float upwards.
     *
     * @param player the player whose gravity is to be reversed
     */
    public static void flipGravity(Player player) {
        player.getAttribute(Attribute.GRAVITY).addModifier(GRAVITY_FLIP_MODIFIER);
    }

    /**
     * Resets the gravity of the player by removing a previously applied gravity flip.
     *
     * @param player the player whose gravity is to be reset
     */
    public static void resetGravity(Player player) {
        player.getAttribute(Attribute.GRAVITY).removeModifier(GRAVITY_FLIP_MODIFIER.id());
    }

    private AttributeHelper() {
        // Nothing to do here
    }
}
