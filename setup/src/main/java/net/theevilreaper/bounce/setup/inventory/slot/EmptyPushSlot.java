package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link EmptyPushSlot} represents a {@link Slot} implementation that is used to indicate that a given push slot is not set up with any data.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 0.1.0
 */
public final class EmptyPushSlot extends Slot {

    /**
     * Creates a new {@link EmptyPushSlot} with the specified {@link ItemStack}.
     *
     * @param itemStack the {@link ItemStack} to be used for this slot, typically representing an empty or default state.
     */
    public EmptyPushSlot(@NotNull ItemStack itemStack) {
        setItemStack(itemStack);
        setClick(InventoryConstants.CANCEL_CLICK);
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }
}
