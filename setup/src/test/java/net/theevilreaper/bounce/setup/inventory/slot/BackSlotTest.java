package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BackSlotTest {

    @Test
    void testBackSlot() {
        BackSlot backSlot = new BackSlot();
        assertNotNull(backSlot);
        assertInstanceOf(BackSlot.class, backSlot);

        ItemStack item = backSlot.getItem();
        assertNotNull(item);
        assertEquals(Material.BARRIER, item.material());
        assertTrue(item.has(ItemComponent.CUSTOM_NAME));

        Component customName = item.get(ItemComponent.CUSTOM_NAME);
        assertNotNull(customName);
        String text = PlainTextComponentSerializer.plainText().serialize(customName);
        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertEquals("Back", text);
    }
}