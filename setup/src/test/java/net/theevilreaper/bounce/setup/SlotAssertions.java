package net.theevilreaper.bounce.setup;

import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ApiStatus.Internal
public final class SlotAssertions {

    /**
     * Asserts that the specified ItemStack is present in the given slots of the InventoryLayout.
     *
     * @param layout   The InventoryLayout to check.
     * @param expected The expected ItemStack.
     * @param slots    The slots to check for the expected ItemStack.
     */
    public static void assertItemStackInRow(@NotNull InventoryLayout layout, @NotNull ItemStack expected, int... slots) {
        for (int slotIndex : slots) {
            ISlot slot = layout.getSlot(slotIndex);
            assertNotNull(slot, "Slot " + slotIndex + " should not be null");
            ItemStack itemStack = slot.getItem();
            assertNotNull(itemStack, "ItemStack in slot " + slot + " should not be null");
            assertEquals(expected, itemStack, "ItemStack in slot " + slot + " should match expected item");
        }
    }

    private SlotAssertions() {
    }

}
