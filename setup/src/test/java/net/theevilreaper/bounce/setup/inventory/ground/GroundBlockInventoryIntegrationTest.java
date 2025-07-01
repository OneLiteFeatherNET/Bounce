package net.theevilreaper.bounce.setup.inventory.ground;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.inventory.SetupBlocks;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class GroundBlockInventoryIntegrationTest {

    @Test
    void testGroundBlockInventoryLayout(@NotNull Env env) {
        GroundBlockInventory groundBlockInventory = new GroundBlockInventory();
        groundBlockInventory.register();

        assertNotNull(groundBlockInventory, "GroundBlockInventory should not be null");

        InventoryLayout layout = groundBlockInventory.getLayout();

        assertNotNull(layout, "InventoryLayout should not be null");
        assertEquals(InventoryType.CHEST_6_ROW.getSize(), layout.getSize(), "Inventory type should be CHEST_6_ROW");

        ItemStack decoration = SetupItems.DECORATION;
        int[] firstRowSlots = LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW);
        int[] lastRowSlots = LayoutCalculator.repeat(InventoryType.CHEST_5_ROW.getSize() + 1, InventoryType.CHEST_6_ROW.getSize() - 2);

        assertItemStackInRow(layout, decoration, firstRowSlots);
        assertItemStackInRow(layout, decoration, lastRowSlots);

        // Check back slot
        ISlot backSlot = layout.getSlot(InventoryType.CHEST_6_ROW.getSize() - 1);
        assertNotNull(backSlot, "Back slot should not be null");
        assertInstanceOf(SwitchTargetSlot.class, backSlot, "Back slot should be an instance of BackSlot");

        int[] itemSlots = LayoutCalculator.quad(InventoryType.CHEST_1_ROW.getSize(), InventoryType.CHEST_5_ROW.getSize() - 1);
        List<Material> allowedGroundBlocks = SetupBlocks.ALLOWED_GROUND_BLOCKS;
        assertNotEquals(allowedGroundBlocks.size(), itemSlots.length, "There should be 36 item slots in the inventory");
        assertTrue(itemSlots.length - allowedGroundBlocks.size() == 2, "The different between item slots and allowed ground blocks should be two");

        for (int i = 0; i < itemSlots.length && i < allowedGroundBlocks.size(); i++) {
            ISlot slot = layout.getSlot(itemSlots[i]);
            assertNotNull(slot, "Slot " + itemSlots[i] + " should not be null");
            ItemStack itemStack = slot.getItem();
            assertNotNull(itemStack, "ItemStack in slot " + itemSlots[i] + " should not be null");
            Material expectedMaterial = allowedGroundBlocks.get(i);
            assertEquals(expectedMaterial, itemStack.material(), "ItemStack in slot " + itemSlots[i] + " should match expected material");
        }

    }

    /**
     * Asserts that the specified ItemStack is present in the given slots of the InventoryLayout.
     *
     * @param layout   The InventoryLayout to check.
     * @param expected The expected ItemStack.
     * @param slots    The slots to check for the expected ItemStack.
     */
    private void assertItemStackInRow(@NotNull InventoryLayout layout, @NotNull ItemStack expected, int... slots) {
        for (int slotIndex : slots) {
            ISlot slot = layout.getSlot(slotIndex);
            assertNotNull(slot, "Slot " + slotIndex + " should not be null");
            ItemStack itemStack = slot.getItem();
            assertNotNull(itemStack, "ItemStack in slot " + slot + " should not be null");
            assertEquals(expected, itemStack, "ItemStack in slot " + slot + " should match expected item");
        }
    }
}