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
}
