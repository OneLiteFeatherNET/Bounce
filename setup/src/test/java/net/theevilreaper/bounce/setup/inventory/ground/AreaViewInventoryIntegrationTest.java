package net.theevilreaper.bounce.setup.inventory.ground;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
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
class AreaViewInventoryIntegrationTest {

    private static final int POS1_SLOT = 11;
    private static final int POS2_SLOT = 13;
    private static final int CONFIRM_SLOT = 15;

    @Test
    void testConfirmSlotStartsDisabledAndBothPositionsAreUnset(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        GameMapBuilder gameMapBuilder = new GameMapBuilder();

        AreaViewInventory inventory = new AreaViewInventory(player, gameMapBuilder);
        inventory.open();
        env.tick();

        InventoryLayout dataLayout = inventory.getDataLayout();
        assertEquals(Material.GRAY_DYE, dataLayout.getSlot(CONFIRM_SLOT).getItem().material());

        env.destroyInstance(instance, true);
    }

    @Test
    void testClickingBothPosButtonsEnablesConfirmAndSavesArea(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        player.teleport(new Pos(1, 2, 3)).join();
        GameMapBuilder gameMapBuilder = new GameMapBuilder();

        AreaViewInventory inventory = new AreaViewInventory(player, gameMapBuilder);
        inventory.open();
        env.tick();

        inventory.setPos1ToCurrentPosition(player);
        inventory.setPos2ToCurrentPosition(player);
        env.tick();

        InventoryLayout dataLayout = inventory.getDataLayout();
        ISlot confirmSlot = dataLayout.getSlot(CONFIRM_SLOT);
        assertEquals(Material.LIME_DYE, confirmSlot.getItem().material());

        inventory.confirm(player);

        assertNotNull(gameMapBuilder.getArea());
        assertEquals(new net.minestom.server.coordinate.Vec(1, 2, 3), gameMapBuilder.getArea().min());
        assertEquals(new net.minestom.server.coordinate.Vec(1, 2, 3), gameMapBuilder.getArea().max());

        env.destroyInstance(instance, true);
    }
}
