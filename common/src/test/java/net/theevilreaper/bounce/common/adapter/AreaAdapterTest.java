package net.theevilreaper.bounce.common.adapter;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
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
                .add(Block.SLIME_BLOCK, 1.0)
                .add(Block.ACACIA_FENCE, 0.5)
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
                        "value": "acacia_fence"
                      },
                      "value": 0.5
                    },
                    {
                      "block": {
                        "namespace": "minecraft",
                        "value": "slime_block"
                      },
                      "value": 1.0
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
        assertEquals(Block.GRASS_BLOCK, area.groundBlock());
        assertEquals(data, area.data());

    }
}