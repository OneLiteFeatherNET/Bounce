package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PushDataTest {

    private static PushData pushData;

    @BeforeAll
    static void setUp() {
        pushData = new PushData(
                List.of(
                        PushEntry.groundEntry(Block.STONE, 1),
                        PushEntry.pushEntry(Block.DIRT, 2),
                        PushEntry.pushEntry(Block.GRASS_BLOCK, 3)
                )
        );
    }

    @Test
    void testPushData() {
        assertNotNull(pushData);
        assertEquals(1.0, pushData.getPush(Block.STONE));
        assertEquals(2.0, pushData.getPush(Block.DIRT));
        assertEquals(3.0, pushData.getPush(Block.GRASS_BLOCK));
        // Test for a block not in the map and expect 0.0
        assertEquals(0.0, pushData.getPush(Block.SAND));
    }

    @Test
    void testInstancesDoNotShareEachOthersPushValues() {
        PushData mapA = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        // Constructing a second, unrelated PushData (e.g. for a different map) must not corrupt mapA.
        PushData mapB = new PushData(List.of(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1, 1.0)));

        assertTrue(mapA.hasBlock(Block.DIAMOND_BLOCK), "mapA should still know about its own DIAMOND_BLOCK entry");
        assertFalse(mapA.hasBlock(Block.EMERALD_BLOCK), "mapA must not see mapB's EMERALD_BLOCK entry");
        assertTrue(mapB.hasBlock(Block.EMERALD_BLOCK), "mapB should know about its own EMERALD_BLOCK entry");
        assertFalse(mapB.hasBlock(Block.DIAMOND_BLOCK), "mapB must not see mapA's DIAMOND_BLOCK entry");
    }
}
