package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.aves.inventory.util.InventoryConstants;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class EmptyPushSlot extends Slot {

    private static final ItemStack EMPTY_ITEM;

    static {
        EMPTY_ITEM = ItemStack.builder(Material.BARRIER)
                .customName(Component.text("No data", NamedTextColor.RED))
                .lore(
                        Component.empty(),
                        miniMessage().deserialize("<gray>Please do a <green>left click <gray>to add data"),
                        Component.empty()
                )
                .build();
    }

    public EmptyPushSlot() {
        setClick(InventoryConstants.CANCEL_CLICK);
    }

    @Override
    public ItemStack getItem() {
        return EMPTY_ITEM;
    }
}
