package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroundSlotTest {

    @Test
    void testSlot() {
        GroundSlot slot = new GroundSlot(OverviewType.GROUND, () -> {
            // Nothing to do here, just a placeholder for the view switcher
        });

        assertNotNull(slot);
        assertEquals(OverviewType.GROUND, slot.type);

        assertNotNull(slot.getItem());

        ItemStack originalItem = OverviewType.GROUND.getItem();
        ItemStack slotItem = slot.getItem();

        assertEquals(originalItem.material(), slotItem.material());
        assertTrue(slotItem.has(ItemComponent.ITEM_NAME));
        assertEquals(originalItem.get(ItemComponent.ITEM_NAME), slotItem.get(ItemComponent.ITEM_NAME));
    }
}