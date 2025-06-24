package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

public class MaterialSlot extends Slot {

    private final ItemStack stack;

    public MaterialSlot(@NotNull Material material) {
        this.stack = ItemStack.builder(material)
                .lore(Component.empty(), SetupMessages.NO_SPACE_SEPARATOR.append(Component.space()).append(Component.text("Ground block", NamedTextColor.GRAY)))
                .build();
        //this.setClick(InventoryConstants.CANCEL_CLICK);
    }

    @Override
    public ItemStack getItem() {
        return stack;
    }
}
