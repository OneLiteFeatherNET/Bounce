package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushEntryTest {

    @Test
    void testSetter() {
        PushEntry pushEntry = PushEntry.groundEntry(Block.STONE, 10);

        pushEntry.setBlock(Block.DIRT);
        assertEquals(Block.DIRT, pushEntry.getBlock(), "Block should be set to DIRT");
        assertNotEquals(Block.STONE, pushEntry.getBlock(), "Block should not be set to STONE");

        pushEntry.setValue(20);
        assertEquals(20, pushEntry.getValue(), "Value should be set to 20");
        assertNotEquals(10, pushEntry.getValue(), "Value should not be set to 10");

        assertTrue(pushEntry.isGround(), "PushEntry should be a ground entry");
    }

    @Test
    void testIncrement() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5);

        pushEntry.incrementValue();
        assertEquals(6, pushEntry.getValue(), "Value should be incremented to 6");
    }

    @Test
    void testIncrementMaxValue() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, Integer.MAX_VALUE);

        pushEntry.incrementValue();
        assertEquals(Integer.MAX_VALUE, pushEntry.getValue(), "Value should not exceed Integer.MAX_VALUE");
    }

    @Test
    void testDecrement() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.GRAVEL, 5);

        pushEntry.decrementValue();
        assertEquals(4, pushEntry.getValue(), "Value should be decremented to 4");
    }

    @Test
    void testDecrementBelowZero() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.COBBLESTONE, 0);

        pushEntry.decrementValue();
        assertEquals(0, pushEntry.getValue(), "Value should not go below zero");
    }

    @Test
    void testEquality() {
        PushEntry entry1 = PushEntry.groundEntry(Block.WARPED_WART_BLOCK, 15);
        PushEntry entry2 = PushEntry.groundEntry(Block.WARPED_WART_BLOCK, 15);

        assertEquals(entry1, entry2, "Entries with the same block and value should be equal");

        entry2.setValue(20);
        assertNotEquals(entry1, entry2, "Entries with different values should not be equal");

        entry2.setBlock(Block.STONE);
        assertNotEquals(entry1, entry2, "Entries with different blocks should not be equal");
    }

    @Test
    void testWeightDefaults() {
        PushEntry ground = PushEntry.groundEntry(Block.SAND, 5);
        assertEquals(1.0, ground.getWeight(), "Ground weight should default to 1.0");

        PushEntry push = PushEntry.pushEntry(Block.SAND, 5);
        assertEquals(0.05, push.getWeight(), "Push weight should default to 0.05");
    }

    @Test
    void testWeightConstructorOverload() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5, 0.25);
        assertEquals(0.25, pushEntry.getWeight());
    }

    @Test
    void testIncrementWeight() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5, 0.05);
        pushEntry.incrementWeight();
        assertEquals(0.06, pushEntry.getWeight());
    }

    @Test
    void testIncrementWeightMaxValue() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5, 1.0);
        pushEntry.incrementWeight();
        assertEquals(1.0, pushEntry.getWeight(), "Weight must not exceed 1.0");
    }

    @Test
    void testDecrementWeight() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5, 0.05);
        pushEntry.decrementWeight();
        assertEquals(0.04, pushEntry.getWeight());
    }

    @Test
    void testDecrementWeightNeverGoesNegative() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5, 0.0);
        pushEntry.decrementWeight();
        assertEquals(0.0, pushEntry.getWeight(), "Weight of 0 must stay 0, it means the entry is never picked");
    }

    @Test
    void testSetWeight() {
        PushEntry pushEntry = PushEntry.pushEntry(Block.SAND, 5);
        pushEntry.setWeight(0.75);
        assertEquals(0.75, pushEntry.getWeight());
    }
}
