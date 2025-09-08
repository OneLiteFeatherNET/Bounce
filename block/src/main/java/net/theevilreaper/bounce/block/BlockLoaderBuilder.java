package net.theevilreaper.bounce.block;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import net.theevilreaper.bounce.block.type.BarrelBlockHandler;
import net.theevilreaper.bounce.block.type.GrindstoneBlockHandler;
import net.theevilreaper.bounce.block.type.lantern.LanternBlockFactory;
import net.theevilreaper.bounce.block.type.player.PlayerHeadBlockHandler;
import net.theevilreaper.bounce.block.type.player.PlayerWallHeadBlockHandler;

public final class BlockLoaderBuilder implements BlockLoader {

    private final BlockManager blockManager;

    BlockLoaderBuilder(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    public static void fromAll(BlockManager blockManager) {
        BlockLoaderBuilder builder = new BlockLoaderBuilder(blockManager);
        builder.loadAll();
    }

    private void loadAll() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader lanternVariant(LanternBlockFactory.Type type) {
        blockManager.registerHandler(type.getBlock().key(), () -> LanternBlockFactory.create(type));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader playerHead() {
        BlockHandler blockHandler = new PlayerHeadBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader playerWallHead() {
        BlockHandler blockHandler = new PlayerWallHeadBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader barrel() {
        BlockHandler blockHandler = new BarrelBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader ironBars() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockLoader grindStone() {
        BlockHandler blockHandler = new GrindstoneBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }
}
