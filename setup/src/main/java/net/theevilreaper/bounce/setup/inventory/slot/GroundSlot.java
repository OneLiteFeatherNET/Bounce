package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

public class GroundSlot extends AbstractDataSlot {

    public GroundSlot(@NotNull OverviewType type) {
        super(type);
    }

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
    }

    @Override
    public ItemStack getItem() {
        return type.getItem();
    }
}
