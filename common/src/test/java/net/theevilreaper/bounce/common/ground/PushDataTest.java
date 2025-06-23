package net.theevilreaper.bounce.common.ground;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PushDataTest {

    @Test
    void testPushData() {
        PushData pushData = new PushData(Map.of(
                Block.STONE, 1.0,
                Block.DIRT, 0.5,
                Block.GRASS_BLOCK, 0.75
        ));

        assertNotNull(pushData);
        assertEquals(1.0, pushData.getPush(Block.STONE));
        assertEquals(0.5, pushData.getPush(Block.DIRT));
        assertEquals(0.75, pushData.getPush(Block.GRASS_BLOCK));
        // Test for a block not in the map and expect 0.0
        assertEquals(0.0, pushData.getPush(Block.SAND));
    }
}