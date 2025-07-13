package net.theevilreaper.bounce.setup.util;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static net.theevilreaper.bounce.setup.util.SetupItems.ITEM_TAG;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class SetupItemsIntegrationTest {

    private static SetupItems setupItems;

    @BeforeAll
    static void setUp() {
        setupItems = new SetupItems();
    }

    @AfterAll
    static void tearDown() {
        setupItems = null;
    }

    @Test
    void testOverViewItem(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        assertNotNull(player);

        setupItems.setOverViewItem(player);
        ItemStack overViewItem = player.getInventory().getItemStack(0x00);
        assertNotNull(overViewItem);
        assertNotEquals(Material.AIR, overViewItem.material());
        assertEquals(Material.CHEST, overViewItem.material());

        assertEquals(0x00, overViewItem.getTag(ITEM_TAG).byteValue());
        assertEquals(0x00, player.getHeldSlot());

        env.destroyInstance(instance, true);
    }

    @Test
    void testSetupItems(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        assertNotNull(player);

        setupItems.setSaveItem(player);
        ItemStack saveItem = player.getInventory().getItemStack(0x06);
        assertNotNull(saveItem);
        assertNotEquals(Material.AIR, saveItem.material());
        assertEquals(Material.BELL, saveItem.material());

        assertEquals(0x01, saveItem.getTag(ITEM_TAG).byteValue());
        assertEquals(0x00, player.getHeldSlot());

        ItemStack groundItem = player.getInventory().getItemStack(0x04);
        assertNotNull(groundItem);
        assertEquals(Material.CARTOGRAPHY_TABLE, groundItem.material());

        env.destroyInstance(instance, true);
    }
}
