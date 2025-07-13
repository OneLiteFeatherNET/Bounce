package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.junit.jupiter.api.Assertions.*;

class EmptyPushSlotTest {

    @Test
    void testSlot() {
        ItemStack stack = ItemStack.builder(Material.BARRIER)
                .customName(Component.text("No data", NamedTextColor.RED))
                .lore(
                        Component.empty(),
                        miniMessage().deserialize("<gray>Please do a <green>left click <gray>to add data"),
                        Component.empty()
                )
                .build();
        EmptyPushSlot slot = new EmptyPushSlot(stack);
        assertNotNull(slot);
        assertInstanceOf(Slot.class, slot);

        assertNotNull(slot.getClick());
        assertEquals(InventoryConstants.CANCEL_CLICK, slot.getClick());

        assertNotNull(slot.getItem());
        assertEquals(Material.BARRIER, slot.getItem().material());

        ItemStack item = slot.getItem();

        assertTrue(item.has(ItemComponent.CUSTOM_NAME));
        assertTrue(item.has(ItemComponent.LORE));

        List<Component> lore = item.get(ItemComponent.LORE);

        assertNotNull(lore);
        assertFalse(lore.isEmpty());
        assertEquals(3, lore.size(), "Lore should contain 3 components");
        assertAll(
                "Test empty components",
                () -> assertEquals(Component.empty(), lore.getFirst(), "First lore component should be empty"),
                () -> assertEquals(Component.empty(), lore.getLast(), "Last lore component should be empty")
        );
    }
}