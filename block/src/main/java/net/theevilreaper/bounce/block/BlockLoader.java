package net.theevilreaper.bounce.block;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import org.jetbrains.annotations.Contract;

import net.theevilreaper.bounce.block.type.lantern.LanternBlockFactory.Type;

public sealed interface BlockLoader permits BlockLoaderBuilder {

    /**
     * Creates a new {@link BlockLoaderBuilder} for the specified block manager.
     *
     * @param blockManager the block manager to use
     * @return a new {@link BlockLoaderBuilder} instance
     */
    @Contract(value = "_ -> new", pure = true)
    static BlockLoader builder(BlockManager blockManager) {
        return new BlockLoaderBuilder(blockManager);
    }

    /**
     * Registers a new torch variant.
     *
     * @param type the type of the torch variant
     * @return this builder instance for chaining
     */
    BlockLoader lanternVariant(Type type);

    /**
     * Registers a new head variant.
     *
     * @return this builder instance for chaining
     */
    BlockLoader playerHead();

    /**
     * Registers a new wall head variant.
     *
     * @return this builder instance for chaining
     */
    BlockLoader playerWallHead();

    /**
     * Registers a new barrel variant.
     *
     * @return this builder instance for chaining
     */
    BlockLoader barrel();

    /**
     * Registers a new iron bars variant.
     *
     * @return this builder instance for chaining
     */
    BlockLoader ironBars();

    /**
     * Registers a new grindstone variant.
     *
     * @return this builder instance for chaining
     */
    BlockLoader grindStone();
}
