package net.theevilreaper.bounce.common.adapter;

import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.common.util.GsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushDataAdapterTest {

    private static final String TEST_JSON = """
            [
                {
                    "block": {
                      "namespace": "minecraft",
                      "value": "slime_block"
                    },
                    "ground": true,
                    "value": 1
                },
                {
                    "block": {
                      "namespace": "minecraft",
                      "value": "amethyst_block"
                    },
                    "ground": false,
                    "value": 2
                }
            ]
            """;

    @Test
    void testPushDataWrite() {
        PushData pushData = PushData.builder()
                .add(PushEntry.groundEntry(Block.SLIME_BLOCK, 1))
                .add(PushEntry.pushEntry(Block.AMETHYST_BLOCK, 2))
                .build();
        assertNotNull(pushData);
        String json = GsonUtil.GSON.toJson(pushData);
        System.out.println(json);
        assertNotNull(json);

    }

    @Test
    void testPushDataRead() {
        PushData pushData = GsonUtil.GSON.fromJson(TEST_JSON, PushData.class);
        assertNotNull(pushData);
        assertEquals(2, pushData.push().size());

        assertEquals(1, pushData.getPush(Block.SLIME_BLOCK));
        assertEquals(2, pushData.getPush(Block.AMETHYST_BLOCK));
    }
}
