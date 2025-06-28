package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

    @Test
    void testArea() {
        Area area = new GroundArea(Vec.ZERO, Vec.ONE, Block.AMETHYST_BLOCK, new PushData(Map.of()));

        assertNotNull(area);
        assertEquals(Vec.ZERO, area.min());
        assertEquals(Vec.ONE, area.max());
        assertEquals(Block.AMETHYST_BLOCK, area.groundBlock());
        assertNotNull(area.data());
        assertNotNull(area.data().push());
        assertTrue(area.data().push().isEmpty());
        assertEquals(0.0, area.data().getPush(area.groundBlock()));

        assertFalse(area.hasPositions());

        area.calculatePositions();

        assertTrue(area.hasPositions());
    }
}
