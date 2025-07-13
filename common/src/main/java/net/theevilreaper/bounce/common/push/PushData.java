package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link PushData} class represents a collection of different push values which are associated with specific blocks.
 *
 * @param push a map where the key is a {@link Block} and the value is a {@code double} representing the push value for that block.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public record PushData(@NotNull List<PushEntry> push) {

    private static Map<Block, Integer> pushMap;

    public PushData {
        pushMap = push.stream()
                .collect(Collectors.toMap(PushEntry::getBlock, PushEntry::getValue, (v1, v2) -> v2));
    }

    /**
     * Checks if the push data contains a specific block.
     * @param block the block to check for presence in the push data
     * @return true when the provided block is in the data otherwise false
     */
    public boolean hasBlock(@NotNull Block block) {
        return pushMap.containsKey(block);
    }

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
    public int getPush(@NotNull Block block) {
        return pushMap.getOrDefault(block, 0);
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
         * Adds a block with its associated push value to the push data.
         *
         * @param entry the {@link PushEntry} containing the block and its push value
         * @return the builder instance for method chaining
         */
        @NotNull Builder add(@NotNull PushEntry entry);

        /**
         * Adds a block with its associated push value at a specific index in the push data.
         *
         * @param index the index at which to add the entry
         * @param entry the {@link PushEntry} containing the block and its push value
         * @return the builder instance for method chaining
         */
        @NotNull Builder add(int index, @NotNull PushEntry entry);


        /**
         * Retrieves the accumulated push values.
         *
         * @return an unmodifiable view of the map containing block push values
         */
        @NotNull
        @UnmodifiableView
        List<PushEntry> getPushValues();

        /**
         * Builds the {@link PushData} instance.
         *
         * @return a new {@link PushData} instance containing the accumulated push values
         */
        @NotNull PushData build();
    }
}
