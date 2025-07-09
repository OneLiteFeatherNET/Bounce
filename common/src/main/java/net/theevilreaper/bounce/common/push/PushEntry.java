package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;

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

    private Block block;
    private int value;

    /**
     * Constructs a new PushEntry with the specified block and value.
     *
     * @param block the block associated with this PushEntry
     * @param value the initial value for this PushEntry
     */
    public PushEntry(Block block, int value) {
        this.block = block;
        this.value = value;
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

    public void setValue(int value) {
        System.out.println("PushEntry value set to: " + value);
        this.value = value;
        System.out.println("PushEntry value is now: " + this.value);
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
        if (this.value < 0) return;
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
    public Block getBlock() {
        return block;
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
