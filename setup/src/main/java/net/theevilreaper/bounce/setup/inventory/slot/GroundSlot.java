package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

public class GroundSlot extends AbstractDataSlot {

    private final VoidConsumer viewSwitcher;

    public GroundSlot(@NotNull OverviewType type, @NotNull VoidConsumer viewSwitcher) {
        super(type);
        this.viewSwitcher = viewSwitcher;
    }

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();
        MinecraftServer.getSchedulerManager().scheduleNextTick(this.viewSwitcher::apply);
    }

    @Override
    public ItemStack getItem() {
        return type.getItem();
    }
}
