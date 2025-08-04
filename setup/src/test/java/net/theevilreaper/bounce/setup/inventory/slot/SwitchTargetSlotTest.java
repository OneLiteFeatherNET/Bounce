package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwitchTargetSlotTest {

    @Test
    void testBackSlot() {
        SwitchTargetSlot switchTargetSlot = new SwitchTargetSlot(SetupInventorySwitchEvent.SwitchTarget.MAP_OVERVIEW);
        assertNotNull(switchTargetSlot);
        assertInstanceOf(SwitchTargetSlot.class, switchTargetSlot);

        ItemStack item = switchTargetSlot.getItem();
        assertNotNull(item);
        assertEquals(Material.BARRIER, item.material());
        assertTrue(item.has(DataComponents.CUSTOM_NAME));

        Component customName = item.get(DataComponents.CUSTOM_NAME);
        assertNotNull(customName);
        String text = PlainTextComponentSerializer.plainText().serialize(customName);
        assertNotNull(text);
        assertFalse(text.isEmpty());
        assertEquals("Back", text);
    }
}