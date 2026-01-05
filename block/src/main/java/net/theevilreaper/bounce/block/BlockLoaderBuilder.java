package net.theevilreaper.bounce.block;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import net.theevilreaper.bounce.block.type.BambooPotBlockHandler;
import net.theevilreaper.bounce.block.type.BarrelBlockHandler;
import net.theevilreaper.bounce.block.type.CandleBlockHandler;
import net.theevilreaper.bounce.block.type.GrindstoneBlockHandler;
import net.theevilreaper.bounce.block.type.IronChainBlockHandler;
import net.theevilreaper.bounce.block.type.SlabBlockHandler;
import net.theevilreaper.bounce.block.type.TorchBlockHandler;
import net.theevilreaper.bounce.block.type.gates.GateBlockHandler;
import net.theevilreaper.bounce.block.type.grates.IronGrateBlockHandler;
import net.theevilreaper.bounce.block.type.lantern.LanternBlockFactory;
import net.theevilreaper.bounce.block.type.player.PlayerHeadBlockHandler;
import net.theevilreaper.bounce.block.type.player.PlayerWallHeadBlockHandler;
import net.theevilreaper.bounce.block.type.stairs.StairsBlockHandler;

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
        BlockHandler blockHandler = new IronGrateBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
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

    @Override
    public BlockLoader ironChain() {
        BlockHandler blockHandler = new IronChainBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    @Override
    public BlockLoader stairs() {
        blockManager.registerHandler(Block.OAK_STAIRS.key(), () -> new StairsBlockHandler(Block.OAK_STAIRS));
        blockManager.registerHandler(Block.SPRUCE_STAIRS.key(), () -> new StairsBlockHandler(Block.OAK_STAIRS));
        blockManager.registerHandler(Block.PALE_OAK_STAIRS.key(), () -> new StairsBlockHandler(Block.OAK_STAIRS));
        return this;
    }

    @Override
    public BlockLoader candle() {
        BlockHandler blockHandler = new CandleBlockHandler(Block.RED_CANDLE.key());
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    @Override
    public BlockLoader torch() {
        BlockHandler blockHandler = new TorchBlockHandler();
        blockManager.registerHandler(blockHandler.getKey(), () -> blockHandler);
        return this;
    }

    @Override
    public BlockLoader fenceGate() {
        blockManager.registerHandler(Block.OAK_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        blockManager.registerHandler(Block.BIRCH_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        blockManager.registerHandler(Block.SPRUCE_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        blockManager.registerHandler(Block.JUNGLE_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        blockManager.registerHandler(Block.ACACIA_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        blockManager.registerHandler(Block.PALE_OAK_FENCE_GATE.key(), () -> new GateBlockHandler(Block.OAK_PLANKS.key()));
        return this;
    }

    @Override
    public BlockLoader slab() {
        blockManager.registerHandler(
                Block.OAK_SLAB.key(), () -> new SlabBlockHandler(Block.OAK_SLAB.key())
        );
        blockManager.registerHandler(
                Block.OAK_SLAB.key(), () -> new SlabBlockHandler(Block.SPRUCE_SLAB.key())
        );
        blockManager.registerHandler(
                Block.OAK_SLAB.key(), () -> new SlabBlockHandler(Block.WARPED_SLAB.key())
        );
        return this;

    }

    @Override
    public BlockLoader flowerPot() {
        blockManager.registerHandler(Block.POTTED_BAMBOO.key(), BambooPotBlockHandler::new);
        return this;
    }
}
