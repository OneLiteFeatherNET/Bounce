package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.slot.Slot;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class SwitchTargetSlot extends Slot {

    private final ItemStack stack;
    private final SwitchTarget switchTarget;

    public SwitchTargetSlot(@NotNull SwitchTarget target) {
        this.stack = ItemStack.builder(Material.BARRIER)
                .customName(Component.text("Back", NamedTextColor.RED))
                .build();
        this.switchTarget = target;
        setClick(this::handleClick);
    }

    /**
     * Handles the click event for the back slot.
     *
     * @param player    the player who clicked
     * @param slot      the slot that was clicked
     * @param clickType the type of click
     * @param result    the result of the click condition
     */
    private void handleClick(@NotNull Player player, int slot, @NotNull Click clickType, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        player.closeInventory();
        EventDispatcher.call(new SetupInventorySwitchEvent(player, this.switchTarget));
    }

    @Override
    public ItemStack getItem() {
        return this.stack;
    }
}
