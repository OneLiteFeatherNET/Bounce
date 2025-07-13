package net.theevilreaper.bounce.setup.inventory.overview;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.theevilreaper.bounce.setup.SlotAssertions.assertSlot;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class MapOverviewInventoryIntegrationTest {

    private static GameMapBuilder gameMapBuilder;
    private static VoidConsumer inventorySwitcher;

    @BeforeAll
    static void setup() {
        gameMapBuilder = new GameMapBuilder();
        inventorySwitcher = () -> {
            throw new RuntimeException("Works");
        };

        assertNotNull(gameMapBuilder);
    }

    @Test
    void testMapOverviewLayout(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        MapOverviewInventory inventory = new MapOverviewInventory(player, gameMapBuilder, inventorySwitcher);

        assertNotNull(inventory);

        InventoryLayout layout = inventory.getLayout();
        assertNotNull(layout, "Inventory layout should not be null");

        int[] slots = LayoutCalculator.quad(0, inventory.getType().getSize() - 1);

        for (int slot : slots) {
            assertSlot(layout, slot, SetupItems.DECORATION.material());
        }

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    @Test
    void testMapOverviewDataLayout(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        MapOverviewInventory inventory = new MapOverviewInventory(player, gameMapBuilder, inventorySwitcher);
        assertNotNull(inventory);

        // We need to open the inventory to initialize the data layout
        inventory.open();

        // Simulate a tick to ensure the inventory is fully initialized
        env.tick();

        InventoryLayout dataLayout = inventory.getDataLayout();

        assertNotNull(dataLayout, "Data layout should not be null");

        int[] dataSlots = LayoutCalculator.from(10, 12, 14, 16);
        OverviewType[] overviewTypes = OverviewType.getValues();

        for (int i = 0; i < overviewTypes.length && i < dataSlots.length; i++) {
            int slotIndex = dataSlots[i];
            OverviewType type = overviewTypes[i];
            ISlot slot = dataLayout.getSlot(slotIndex);
            assertNotNull(slot, "Slot at index " + slotIndex + " should not be null");
            ItemStack item = slot.getItem();
            assertNotNull(item, "ItemStack in slot " + slotIndex + " should not be null");
            assertEquals(type.getMaterial(), item.material(), "Slot material should match the overview type");
        }

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }
}
