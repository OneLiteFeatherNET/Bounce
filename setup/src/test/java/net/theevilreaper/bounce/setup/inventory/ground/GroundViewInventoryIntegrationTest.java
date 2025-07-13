package net.theevilreaper.bounce.setup.inventory.ground;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class GroundViewInventoryIntegrationTest {

    private static GameMapBuilder gameMapBuilder;

    @BeforeEach
    void setUp() {
        gameMapBuilder = new GameMapBuilder();
    }

    @Test
    void testGroundViewInventory(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        GroundViewInventory groundViewInventory = new GroundViewInventory(player, gameMapBuilder);

        assertNotNull(groundViewInventory);
        assertNotNull(groundViewInventory.getLayout());

        InventoryLayout layout = groundViewInventory.getLayout();

        int[] slots = LayoutCalculator.from(12, 14, 16);

        for (int slot : slots) {
            assertSlot(layout, slot, SetupItems.DECORATION.material());
        }

        int[] orangeSlots = LayoutCalculator.fillColumn(InventoryType.CHEST_3_ROW, 2);

        for (int slot : orangeSlots) {
            assertSlot(layout, slot, Material.ORANGE_STAINED_GLASS_PANE);
        }

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    /**
     * Asserts that the slot at the given index in the layout contains an ItemStack with the expected material.
     *
     * @param layout           the InventoryLayout to check
     * @param slotIndex        the index of the slot to check
     * @param expectedMaterial the expected Material of the ItemStack in the slot
     */
    private void assertSlot(@NotNull InventoryLayout layout, int slotIndex, @NotNull Material expectedMaterial) {
        ISlot slot = layout.getSlot(slotIndex);
        assertNotNull(slot, "Slot " + slotIndex + " should not be null");
        ItemStack itemStack = slot.getItem();
        assertNotNull(itemStack, "ItemStack in slot " + slotIndex + " should not be null");
        assertEquals(expectedMaterial, itemStack.material(), "Item in slot " + slotIndex + " should match expected material");
    }
}