package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.slot.Slot;

public final class BackSlot extends Slot {

    private final ItemStack stack;

    public BackSlot() {
        this.stack = ItemStack.builder(Material.BARRIER)
                .customName(Component.text("Back", NamedTextColor.RED))
                .build();
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }
}
