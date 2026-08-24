package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;

import java.util.Objects;

/**
 * The {@link PushEntry} class represents an entry in the push system.
 * It contains a block and a value associated with that block.
 * This class is used to manage the push values in the game.
 *
 * @author Joltra
 * @version 1.1.0
 * @since 0.1.0
 */
public final class PushEntry {

    private final boolean ground;
    private Block block;
    private int value;
    private double weight;

    /**
     * Constructs a new PushEntry with the specified block and value. Uses default weight (1.0 for ground, 0.05 for push).
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @return a new PushEntry instance representing a ground block entry
     */
    public static PushEntry groundEntry(Block block, int value) {
       return new PushEntry(block, value, 1.0, true);
    }

    /**
     * Constructs a new PushEntry with the specified block, value and weight.
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @param weight the probability (0.0 to 1.0) used when this entry is picked during area filling
     * @return a new PushEntry instance representing a ground block entry
     */
    public static PushEntry groundEntry(Block block, int value, double weight) {
        return new PushEntry(block, value, weight, true);
    }

    /**
     * Constructs a new PushEntry with the specified block and value. Uses a default weight of {@code 0.05} (5%).
     *
     * @param block the block associated with this PushEntry
     * @param value the initial value for this PushEntry
     * @return a new PushEntry instance
     */
    public static PushEntry pushEntry(Block block, int value) {
        return new PushEntry(block, value, 0.05, false);
    }

    /**
     * Constructs a new PushEntry with the specified block, value and weight.
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @param weight the probability (0.0 to 1.0) used when this entry is picked during area filling
     * @return a new PushEntry instance
     */
    public static PushEntry pushEntry(Block block, int value, double weight) {
        return new PushEntry(block, value, weight, false);
    }

    /**
     * Constructs a new PushEntry with a default weight.
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @param ground indicates whether this entry is a ground block entry
     */
    public PushEntry(Block block, int value, boolean ground) {
        this(block, value, ground ? 1.0 : 0.05, ground);
    }

    /**
     * Constructs a new PushEntry with the specified block, value, weight and ground flag.
     *
     * @param block  the block associated with this PushEntry
     * @param value  the initial value for this PushEntry
     * @param weight the probability (0.0 to 1.0) used when this entry is picked during area filling
     * @param ground indicates whether this entry is a ground block entry
     */
    public PushEntry(Block block, int value, double weight, boolean ground) {
        this.block = block;
        this.value = value;
        this.weight = clampWeight(weight);
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
     * Sets the weight (probability 0.0 to 1.0) for this PushEntry.
     *
     * @param weight the new weight to set
     */
    public void setWeight(double weight) {
        this.weight = clampWeight(weight);
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
     * Increments the weight by 0.01 (1%), clamped at 1.0.
     */
    public void incrementWeight() {
        this.weight = clampWeight(this.weight + 0.01);
    }

    /**
     * Decrements the weight by 0.01 (1%), clamped at 0.0.
     */
    public void decrementWeight() {
        this.weight = clampWeight(this.weight - 0.01);
    }

    private static double clampWeight(double w) {
        return Math.max(0.0, Math.min(1.0, Math.round(w * 100.0) / 100.0));
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
     * Gets the current weight of this PushEntry as a probability between 0.0 and 1.0.
     *
     * @return the current weight of this PushEntry
     */
    public double getWeight() {
        return weight;
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
