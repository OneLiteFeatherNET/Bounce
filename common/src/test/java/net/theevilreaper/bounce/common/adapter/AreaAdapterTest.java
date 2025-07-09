package net.theevilreaper.bounce.common.adapter;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.common.util.GsonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AreaAdapterTest {

    private static PushData data;
    private static String areaJson;

    @BeforeAll
    static void setup() {
        data = PushData.builder()
                .add(PushEntry.pushEntry(Block.SLIME_BLOCK, 2))
                .add(PushEntry.groundEntry(Block.GRASS_BLOCK, 1))
                .build();
        areaJson = """
                {
                  "min": {
                    "x": 0.0,
                    "y": 0.0,
                    "z": 0.0
                  },
                  "max": {
                    "x": 5.0,
                    "y": 0.0,
                    "z": 5.0
                  },
                  "data": [
                    {
                      "block": {
                        "namespace": "minecraft",
                        "value": "slime_block"
                      },
                      "value": 2,
                      "ground": false
                    },
                    {
                      "block": {
                        "namespace": "minecraft",
                        "value": "grass_block"
                      },
                      "value": 1,
                      "ground": true
                    }
                  ],
                  "groundBlock": {
                    "namespace": "minecraft",
                    "value": "grass_block"
                  }
                }
                """;
    }

    @Test
    void testAreaWrite() {
        Area area = new GroundArea(Vec.ZERO, new Vec(5, 0, 5), Block.GRASS_BLOCK, data);
        assertNotNull(area);
        String json = GsonUtil.GSON.toJson(area, Area.class);
        System.out.println(json);
        assertNotNull(json);
        assertAll(
                "Contains checks for the json",
                () -> assertTrue(json.contains("min")),
                () -> assertTrue(json.contains("max")),
                () -> assertTrue(json.contains("groundBlock")),
                () -> assertTrue(json.contains("data"))
        );
    }

    @Test
    void testAreaRead() {
        Area area = GsonUtil.GSON.fromJson(areaJson, Area.class);
        assertNotNull(area);
        assertEquals(Vec.ZERO, area.min());
        assertEquals(new Vec(5, 0, 5), area.max());

        assertNotNull(area.data());
        PushEntry groundBlock = area.data().push().stream().filter(PushEntry::isGround).findAny().orElse(null);

        assertNotNull(groundBlock, "Ground block entry should not be null");
        assertEquals(Block.GRASS_BLOCK, groundBlock.getBlock());
    }
}
