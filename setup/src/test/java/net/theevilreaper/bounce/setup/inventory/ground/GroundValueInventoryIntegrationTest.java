package net.theevilreaper.bounce.setup.inventory.ground;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
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
class GroundValueInventoryIntegrationTest {

    private static final int BLOCK_SLOT = 11;
    private static final int WEIGHT_SLOT = 13;

    @Test
    void testWeightSlotShowsCurrentWeight(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        GameMapBuilder gameMapBuilder = new GameMapBuilder();

        GroundValueInventory inventory = new GroundValueInventory(player, gameMapBuilder);
        inventory.open();
        env.tick();

        InventoryLayout dataLayout = inventory.getDataLayout();
        ISlot slot = dataLayout.getSlot(WEIGHT_SLOT);
        assertNotNull(slot);
        ItemStack item = slot.getItem();
        assertNotNull(item);
        assertEquals(Material.NETHER_STAR, item.material());

        env.destroyInstance(instance, true);
    }

    @Test
    void testBlockSlotUpdatesAfterChangeWhileClosed(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        GameMapBuilder gameMapBuilder = new GameMapBuilder();

        GroundValueInventory inventory = new GroundValueInventory(player, gameMapBuilder);
        inventory.open();
        env.tick();
        player.closeInventory();
        env.tick();

        gameMapBuilder.groundBlock(Block.STONE);
        inventory.invalidateDataLayout();

        inventory.open();
        env.tick();

        ItemStack item = inventory.getDataLayout().getSlot(BLOCK_SLOT).getItem();
        assertEquals(Material.STONE, item.material(), "Block slot should reflect the ground block changed while the inventory was closed");

        env.destroyInstance(instance, true);
    }
}
