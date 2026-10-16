package net.theevilreaper.bounce.common.push;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushPhysicsTest {

    @Test
    void testVelocity() {
        assertEquals(30D, PushPhysics.velocity(3), "Push value 3 should result in 30 blocks per second");
        assertEquals(0D, PushPhysics.velocity(0), "Push value 0 should not result in any velocity");
    }

    @Test
    void testPeakHeightWithoutPush() {
        assertEquals(0D, PushPhysics.peakHeight(0), "Push value 0 should not lift the player");
    }

    @Test
    void testPeakHeight() {
        assertEquals(1.7, PushPhysics.peakHeight(1), 0.1);
        assertEquals(12.1, PushPhysics.peakHeight(3), 0.1);
        assertEquals(29.3, PushPhysics.peakHeight(5), 0.1);
    }

    @Test
    void testPeakHeightIncreasesWithValue() {
        assertTrue(PushPhysics.peakHeight(4) > PushPhysics.peakHeight(3), "A higher push value should result in a higher jump");
    }
}
