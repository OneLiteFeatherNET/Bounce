package net.theevilreaper.bounce.setup.inventory.slot;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GroundSlot} class represents a slot implementation which is used to indicate the ground layer selection.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 0.1.0
 */
public final class GroundSlot extends AbstractDataSlot {

    private final VoidConsumer viewSwitcher;

    /**
     * Constructs a new {@link GroundSlot} with the specified overview type and view switcher.
     *
     * @param type         the overview type associated with this slot
     * @param viewSwitcher a consumer that switches the view when the slot is clicked
     */
    public GroundSlot(@NotNull OverviewType type, @NotNull VoidConsumer viewSwitcher) {
        super(type);
        this.viewSwitcher = viewSwitcher;
    }

    /**
     * Handles the click event for the ground slot.
     *
     * @param player    the player who clicked the slot
     * @param slot      the slot index that was clicked
     * @param clickType the given click type
     * @param result    the result of the inventory condition check
     */
    @Override
    protected void click(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();
        MinecraftServer.getSchedulerManager().scheduleNextTick(this.viewSwitcher::apply);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ItemStack getItem() {
        return type.getItem();
    }
}
