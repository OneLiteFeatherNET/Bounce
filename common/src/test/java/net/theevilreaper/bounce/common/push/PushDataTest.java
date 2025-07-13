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
}
