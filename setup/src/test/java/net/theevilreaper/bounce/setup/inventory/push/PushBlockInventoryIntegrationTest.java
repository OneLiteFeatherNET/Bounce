package net.theevilreaper.bounce.setup.inventory.push;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.inventory.SetupBlocks;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static net.theevilreaper.bounce.setup.SlotAssertions.assertItemStackInRow;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PushBlockInventoryIntegrationTest {

    @Test
    void testPushBlockInventoryLayout(@NotNull Env env) {
        PushBlockInventory pushBlockInventory = new PushBlockInventory();
        pushBlockInventory.register();

        assertNotNull(pushBlockInventory, "PushBlockInventory should not be null");

        InventoryLayout layout = pushBlockInventory.getLayout();
        assertNotNull(layout, "InventoryLayout should not be null");
        assertEquals(InventoryType.CHEST_3_ROW.getSize(), layout.getSize(), "Inventory type should be CHEST_3_ROW");

        // Check decoration items

        ItemStack decoration = SetupItems.DECORATION;
        int[] firstRowSlots = LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW);
        int[] lastRowSlots = LayoutCalculator.repeat(InventoryType.CHEST_2_ROW.getSize() + 1, InventoryType.CHEST_3_ROW.getSize() - 2);

        assertItemStackInRow(layout, decoration, firstRowSlots);
        assertItemStackInRow(layout, decoration, lastRowSlots);

        List<Material> allowedPushBlocks = SetupBlocks.ALLOWED_PUSH_BLOCKS;

        int[] itemSlots = LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize(),
                InventoryType.CHEST_2_ROW.getSize() - 1
        );

        assertEquals(allowedPushBlocks.size(), itemSlots.length, "There should be 9 item slots in the inventory");
        assertEquals(0, itemSlots.length - allowedPushBlocks.size(), "The difference between item slots and allowed push blocks should zero");
        for (int i = 0; i < itemSlots.length && i < allowedPushBlocks.size() - 1; i++) {
            ISlot slot = layout.getSlot(itemSlots[i]);
            assertNotNull(slot, "Slot " + itemSlots[i] + " should not be null");
            ItemStack itemStack = slot.getItem();
            assertNotNull(itemStack, "ItemStack in slot " + itemSlots[i] + " should not be null");
            assertEquals(allowedPushBlocks.get(i), itemStack.material(), "Item in slot " + itemSlots[i] + " should match allowed push block");
        }

        ISlot lastSlotFromSecondRow = layout.getSlot(InventoryType.CHEST_2_ROW.getSize() - 1);
        assertNotNull(lastSlotFromSecondRow, "Last slot from second row) should not be null");
        assertInstanceOf(ISlot.class, lastSlotFromSecondRow, "Last slot from second row should be an instance of ISlot");

        ItemStack lastSlotItem = lastSlotFromSecondRow.getItem();
        assertNotNull(lastSlotItem, "Item in last slot from second row should not be null");
        assertEquals(decoration.material(), lastSlotItem.material(), "Item in last slot from second row should be decoration item");
    }
}