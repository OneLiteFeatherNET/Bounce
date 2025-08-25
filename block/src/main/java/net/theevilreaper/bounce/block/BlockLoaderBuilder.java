package net.theevilreaper.bounce.block;

import net.minestom.server.instance.block.BlockManager;
import net.theevilreaper.bounce.block.type.lantern.LanternBlockFactory;

public final class BlockLoaderBuilder implements BlockLoader {

    private final BlockManager blockManager;

    BlockLoaderBuilder(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader lanternVariant(LanternBlockFactory.Type type) {
        blockManager.registerHandler(type.getBlock().key(), () -> LanternBlockFactory.create(type));
        return this;
    }
}
