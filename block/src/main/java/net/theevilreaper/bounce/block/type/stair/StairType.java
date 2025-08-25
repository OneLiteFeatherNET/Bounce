package net.theevilreaper.bounce.block.type.stair;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public enum StairType {

    OAK(Block.OAK_STAIRS),
    SPRUCE(Block.SPRUCE_STAIRS),
    BIRCH(Block.BIRCH_STAIRS),
    JUNGLE(Block.JUNGLE_STAIRS),
    ACACIA(Block.ACACIA_STAIRS),
    DARK_OAK(Block.DARK_OAK_STAIRS);

    private final Block block;

    StairType(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

}
