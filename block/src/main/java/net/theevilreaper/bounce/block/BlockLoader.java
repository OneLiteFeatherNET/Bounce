package net.theevilreaper.bounce.block;

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
}
