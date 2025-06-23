package net.theevilreaper.bounce.common.ground;

import net.minestom.server.instance.block.Block;
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
     * Returns the push value for a specific block.
     *
     * @param block the block for which the push value is requested
     * @return the push value associated with the block, or 0.0 if the block is not present in the map
     */
    public double getPush(@NotNull Block block) {
        return push.getOrDefault(block, 0.0);
    }
}
