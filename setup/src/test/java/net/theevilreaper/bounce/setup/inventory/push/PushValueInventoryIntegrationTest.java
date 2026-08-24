package net.theevilreaper.bounce.setup.inventory.push;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PushValueInventoryIntegrationTest {

    private static final int WEIGHT_SLOT = 13;

    @Test
    void testWeightSlotShowsCurrentWeight(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        GameMapBuilder gameMapBuilder = new GameMapBuilder();

        PushValueInventory inventory = new PushValueInventory(player, gameMapBuilder);
        inventory.open();
        inventory.updateLayout(1); // index 0 is the ground entry, 1 is the first push entry
        env.tick();

        InventoryLayout dataLayout = inventory.getDataLayout();
        ISlot slot = dataLayout.getSlot(WEIGHT_SLOT);
        assertNotNull(slot);
        ItemStack item = slot.getItem();
        assertNotNull(item);
        assertEquals(Material.NETHER_STAR, item.material());

        env.destroyInstance(instance, true);
    }
}
