package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDataSlot extends Slot {

    protected final OverviewType type;

    protected AbstractDataSlot(@NotNull OverviewType type) {
        this.type = type;
        this.setClick(this::click);
    }

    protected abstract void click(
            @NotNull Player player,
            int slot,
            @NotNull ClickType clickType,
            @NotNull InventoryConditionResult result
    );

    /**
     * Converts the given ItemStack to a builder, allowing for modifications
     * without altering the original stack.
     *
     * @param stack the ItemStack to convert
     * @return a new ItemStack.Builder with the same material and custom name
     */
    protected @NotNull ItemStack.Builder asBuilder(@NotNull ItemStack stack) {
        ItemStack.Builder builder = ItemStack.builder(stack.material());
        if (stack.has(ItemComponent.CUSTOM_NAME)) {
            builder.customName(stack.get(ItemComponent.CUSTOM_NAME));
        }
        return builder;
    }
}
