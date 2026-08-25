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

    private static final String TEST_JSON_WITH_WEIGHT = """
            [
                {
                    "block": {
                      "namespace": "minecraft",
                      "value": "slime_block"
                    },
                    "ground": false,
                    "value": 1,
                    "weight": 0.15
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

    @Test
    void testPushDataReadDefaultsMissingWeightToOne() {
        PushData pushData = GsonUtil.GSON.fromJson(TEST_JSON, PushData.class);
        assertEquals(1.0, pushData.push().getFirst().getWeight(), "Old maps without a weight field must default to 1.0 for ground");
    }

    @Test
    void testPushDataReadKeepsExplicitWeight() {
        PushData pushData = GsonUtil.GSON.fromJson(TEST_JSON_WITH_WEIGHT, PushData.class);
        assertEquals(0.15, pushData.push().getFirst().getWeight());
    }

    @Test
    void testPushDataWriteIncludesWeight() {
        PushData pushData = PushData.builder()
                .add(PushEntry.pushEntry(Block.SLIME_BLOCK, 1, 0.25))
                .build();
        String json = GsonUtil.GSON.toJson(pushData);
        assertTrue(json.contains("\"weight\": 0.25"));
    }
}
