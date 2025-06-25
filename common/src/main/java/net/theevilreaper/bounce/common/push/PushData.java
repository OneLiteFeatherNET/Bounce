package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The {@link PushData} class represents a collection of different push values which are associated with specific blocks.
 *
 * @param push a map where the key is a {@link Block} and the value is a {@code double} representing the push value for that block.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public record PushData(@NotNull Map<Block, Double> push) {

    /**
     * Creates a new {@link PushData} instance with the provided push values.
     *
     * @return a new {@link PushData.Builder} instance to build a {@link PushData} object
     */
    @Contract(pure = true)
    public static @NotNull PushData.Builder builder() {
        return new PushDataBuilder();
    }

    /**
     * Creates a new {@link PushData.Builder} instance initialized with the provided {@link PushData}.
     *
     * @param pushData the existing {@link PushData} to initialize the builder with
     * @return a new {@link PushData.Builder} instance containing the push values from the provided {@link PushData}
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PushData.Builder builder(@NotNull PushData pushData) {
        return new PushDataBuilder(pushData);
    }

    /**
     * Returns the push value for a specific block.
     *
     * @param block the block for which the push value is requested
     * @return the push value associated with the block, or 0.0 if the block is not present in the map
     */
    public double getPush(@NotNull Block block) {
        return push.getOrDefault(block, 0.0);
    }

    /**
     * The {@link Builder} interface is used to construct a {@link PushData} instance.
     * It allows adding push values for specific blocks and building the final {@link PushData} object.
     * This interface is sealed to restrict its implementation to the {@link PushDataBuilder} class.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 0.1.0
     */
    public sealed interface Builder permits PushDataBuilder {
        /**
         * Adds a push value for a specific block.
         *
         * @param block the block to add the push value for
         * @param value the push value to associate with the block
         * @return the builder instance for method chaining
         */
        @NotNull Builder add(@NotNull Block block, double value);

        /**
         * Removes a block from the push data.
         *
         * @param block the block to remove
         * @return the builder instance for method chaining
         */
        @NotNull Builder remove(@NotNull Block block);

        /**
         * Builds the {@link PushData} instance.
         *
         * @return a new {@link PushData} instance containing the accumulated push values
         */
        @NotNull PushData build();
    }
}
