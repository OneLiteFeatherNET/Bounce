package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The {@link PushEntry} class represents an entry in the push system.
 * It contains a block and a value associated with that block.
 * This class is used to manage the push values in the game.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 0.1.0
 */
public final class PushEntry {

    private final boolean ground;
    private Block block;
    private int value;

    /**
     * Constructs a new PushEntry with the specified block and value.
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @return a new PushEntry instance representing a ground block entry
     */
    public static @NotNull PushEntry groundEntry(@NotNull Block block, int value) {
       return new PushEntry(block, value, true);
    }

    /**
     * Constructs a new PushEntry with the specified block and value.
     *
     * @param block the block associated with this PushEntry
     * @param value the initial value for this PushEntry
     * @return a new PushEntry instance
     */
    public static @NotNull PushEntry pushEntry(@NotNull Block block, int value) {
        return new PushEntry(block, value, false);
    }

    /**
     * Constructs a new PushEntry with the specified block and value.
     *
     * @param block the block associated with this PushEntry
     * @param value the initial value for this PushEntry
     * @param ground indicates whether this entry is a ground block entry
     */
    public PushEntry(Block block, int value, boolean ground) {
        this.block = block;
        this.value = value;
        this.ground = ground;
    }

    /**
     * Sets the block for this PushEntry.
     * This method should be used with caution as it can change the block associated with this entry.
     *
     * @param block the new block to set
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * Sets the value for this PushEntry.
     * This method should be used to update the value associated with this entry.
     *
     * @param value the new value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Increments the value of this PushEntry.
     * If the value is already at Integer.MAX_VALUE, it does nothing.
     */
    public void incrementValue() {
        if (this.value == Integer.MAX_VALUE) return;
        this.value++;
    }

    /**
     * Decrements the value of this PushEntry.
     * If the value is already 0 or negative, it does nothing.
     */
    public void decrementValue() {
        if (this.value <= 0) return;
        this.value--;
    }

    /**
     * Gets the current value of this PushEntry.
     * This method should be used to retrieve the value for display or processing.
     *
     * @return the current value of this PushEntry
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the block associated with this PushEntry.
     * This method should be used to retrieve the block for display or processing.
     *
     * @return the block associated with this PushEntry
     */
    public @NotNull Block getBlock() {
        return block;
    }

    /**
     * Checks if this PushEntry is a ground block entry.
     * This method can be used to differentiate between ground and non-ground entries.
     *
     * @return true if this entry is a ground block entry, false otherwise
     */
    public boolean isGround() {
        return ground;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PushEntry pushEntry)) return false;
        return value == pushEntry.value && Objects.equals(block, pushEntry.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, value);
    }
}
