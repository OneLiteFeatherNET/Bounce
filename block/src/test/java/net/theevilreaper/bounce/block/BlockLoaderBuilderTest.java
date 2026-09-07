package net.theevilreaper.bounce.block;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockLoaderBuilderTest {

    private BlockManager blockManager;
    private BlockLoader blockLoader;

    @BeforeEach
    void setUp() {
        blockManager = new BlockManager();
        blockLoader = BlockLoader.builder(blockManager);
    }

    @Test
    void testStairsRegistration() {
        blockLoader.stairs();

        BlockHandler oakHandler = blockManager.getHandler(Block.OAK_STAIRS.name());
        assertNotNull(oakHandler, "OAK_STAIRS handler should be registered");
        assertEquals(Block.OAK_STAIRS.key(), oakHandler.getKey());

        BlockHandler spruceHandler = blockManager.getHandler(Block.SPRUCE_STAIRS.name());
        assertNotNull(spruceHandler, "SPRUCE_STAIRS handler should be registered");
        assertEquals(Block.SPRUCE_STAIRS.key(), spruceHandler.getKey());

        BlockHandler paleOakHandler = blockManager.getHandler(Block.PALE_OAK_STAIRS.name());
        assertNotNull(paleOakHandler, "PALE_OAK_STAIRS handler should be registered");
        assertEquals(Block.PALE_OAK_STAIRS.key(), paleOakHandler.getKey());
    }

    @Test
    void testSlabRegistration() {
        blockLoader.slab();

        BlockHandler oakSlabHandler = blockManager.getHandler(Block.OAK_SLAB.name());
        assertNotNull(oakSlabHandler, "OAK_SLAB handler should be registered");
        assertEquals(Block.OAK_SLAB.key(), oakSlabHandler.getKey());

        BlockHandler spruceSlabHandler = blockManager.getHandler(Block.SPRUCE_SLAB.name());
        assertNotNull(spruceSlabHandler, "SPRUCE_SLAB handler should be registered");
        assertEquals(Block.SPRUCE_SLAB.key(), spruceSlabHandler.getKey());

        BlockHandler warpedSlabHandler = blockManager.getHandler(Block.WARPED_SLAB.name());
        assertNotNull(warpedSlabHandler, "WARPED_SLAB handler should be registered");
        assertEquals(Block.WARPED_SLAB.key(), warpedSlabHandler.getKey());
    }
}
