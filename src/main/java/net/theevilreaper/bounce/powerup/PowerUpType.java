package net.theevilreaper.bounce.powerup;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.ServerFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

/**
 * The {@link PowerUpType} enum contains every power-up which can spawn during a round.
 * Each type defines the item which is displayed and how long the effect lasts after a pickup.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public enum PowerUpType {

    /**
     * Multiplies the strength of every bounce pad push.
     */
    SUPER_JUMP(Material.RABBIT_FOOT, "Super Jump", NamedTextColor.GREEN, 10),
    /**
     * Grants the speed potion effect.
     */
    SPEED(Material.SUGAR, "Speed", NamedTextColor.AQUA, 10),
    /**
     * Increases the knockback the player deals to other players.
     */
    KNOCKBACK_BOOST(Material.BLAZE_ROD, "Knockback Boost", NamedTextColor.RED, 10),
    /**
     * Makes the player immune against knockback from other players.
     */
    SHIELD(Material.SHIELD, "Shield", NamedTextColor.BLUE, 8),
    /**
     * Doubles all points the player receives.
     */
    DOUBLE_POINTS(Material.GOLD_INGOT, "Double Points", NamedTextColor.GOLD, 15),
    /**
     * Blinds every other player of the round. Unlike the other types, it affects the opponents instead of the
     * player who picked it up.
     */
    BLOOPER(Material.INK_SAC, "Blooper", NamedTextColor.DARK_GRAY, 4);

    private final ItemStack icon;
    private final Component displayName;
    private final int durationSeconds;

    PowerUpType(Material material, String name, TextColor color, int durationSeconds) {
        this.icon = ItemStack.of(material);
        this.displayName = Component.text(name, color);
        this.durationSeconds = durationSeconds;
    }

    /**
     * Returns the item which is displayed by the floating power-up entity.
     *
     * @return the icon as {@link ItemStack}
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Returns the colored name of the power-up.
     *
     * @return the display name
     */
    public Component getDisplayName() {
        return displayName;
    }

    /**
     * Returns how long the effect lasts after a pickup.
     *
     * @return the duration in seconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Returns how long the effect lasts after a pickup.
     *
     * @return the duration in ticks
     */
    public int getDurationTicks() {
        return durationSeconds * ServerFlag.SERVER_TICKS_PER_SECOND;
    }
}
