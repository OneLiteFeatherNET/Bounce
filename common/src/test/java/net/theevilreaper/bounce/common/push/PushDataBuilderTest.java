package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushDataBuilderTest {

    @Test
    void testCopyConstructorPreservesWeight() {
        PushData original = PushData.builder()
                .add(PushEntry.groundEntry(Block.GLASS, 1, 0.8))
                .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 3, 0.2))
                .build();

        PushData copy = PushData.builder(original).build();

        assertEquals(0.8, copy.push().get(0).getWeight());
        assertEquals(0.2, copy.push().get(1).getWeight());
    }
}
