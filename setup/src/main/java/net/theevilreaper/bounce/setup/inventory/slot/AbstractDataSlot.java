package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AbstractDataSlot extends Slot {

    protected final OverviewType type;

    protected AbstractDataSlot(@NotNull OverviewType type) {
        this.type = type;
        this.setClick(this::click);
    }

    protected abstract void click(
            @NotNull Player player,
            int slot,
            @NotNull Click clickType,
            @NotNull ItemStack stack,
            @NotNull Consumer<ClickHolder> result
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
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            builder.customName(stack.get(DataComponents.CUSTOM_NAME));
        }
        return builder;
    }
}
