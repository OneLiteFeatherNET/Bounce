package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.map.BaseMapBuilder;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class StringSlotTest {

    @Test
    void testDefaultNameHasNoLore(@NotNull Env env) {
        StringSlot slot = new StringSlot(OverviewType.NAME, BaseMapBuilder.DEFAULT_NAME);
        ItemStack item = slot.getItem();
        assertEquals(OverviewType.NAME.getItem(), item, "The default map name should not be rendered as a set value");
    }

    @Test
    void testUnsetNameHasNoLore(@NotNull Env env) {
        StringSlot slot = new StringSlot(OverviewType.NAME, null);
        ItemStack item = slot.getItem();
        assertEquals(OverviewType.NAME.getItem(), item);
    }

    @Test
    void testCustomNameHasLore(@NotNull Env env) {
        StringSlot slot = new StringSlot(OverviewType.NAME, "My Map");
        ItemStack item = slot.getItem();
        assertTrue(item.has(DataComponents.LORE));
    }
}
