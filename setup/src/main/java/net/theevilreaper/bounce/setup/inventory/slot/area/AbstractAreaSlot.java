package net.theevilreaper.bounce.setup.inventory.slot.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.bounce.setup.inventory.area.AreaViewInventory;
import net.theevilreaper.bounce.setup.inventory.area.AreaViewType;

import java.util.function.Consumer;

/**
 * Base class for {@link AreaViewType}-bound slots shown in the {@link AreaViewInventory}.
 */
public abstract class AbstractAreaSlot extends Slot {

    protected static final Component CLICK_TO_EDIT = Component.text("Click to edit", NamedTextColor.GRAY);
    protected final AreaViewType type;

    protected AbstractAreaSlot(AreaViewType type) {
        this.type = type;
        this.setClick(this::click);
    }

    protected abstract void click(
            Player player,
            int slot,
            Click clickType,
            ItemStack stack,
            Consumer<ClickHolder> result
    );

    /**
     * Converts the given ItemStack to a builder, allowing for modifications
     * without altering the original stack.
     *
     * @param stack the ItemStack to convert
     * @return a new ItemStack.Builder with the same material and custom name
     */
    protected ItemStack.Builder asBuilder(ItemStack stack) {
        ItemStack.Builder builder = ItemStack.builder(stack.material());
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            builder.customName(stack.get(DataComponents.CUSTOM_NAME));
        }
        return builder;
    }
}
