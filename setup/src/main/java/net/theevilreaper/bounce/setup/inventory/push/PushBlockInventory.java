package net.theevilreaper.bounce.setup.inventory.push;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.GlobalInventoryBuilder;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.event.push.PlayerPushBlockSelectEvent;
import net.theevilreaper.bounce.setup.inventory.SetupBlocks;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * The {@link  PushBlockInventory} class represents an inventory for selecting push blocks in the game.
 * It extends the {@link GlobalInventoryBuilder} to create a custom inventory layout for push block selection
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public final class PushBlockInventory extends GlobalInventoryBuilder {

    public PushBlockInventory() {
        super(Component.text("Select push block"), InventoryType.CHEST_3_ROW);
        InventoryLayout layout = InventoryLayout.fromType(getType());

        int[] itemSlots = LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW);

        List<Material> allowedPushBlocks = SetupBlocks.ALLOWED_PUSH_BLOCKS;

        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), SetupItems.DECORATION);

        for (int i = 0; i < itemSlots.length && i < allowedPushBlocks.size(); i++) {
            Material currentMaterial = allowedPushBlocks.get(i);
            ItemStack stack = ItemStack.builder(currentMaterial)
                    .customName(Component.translatable(currentMaterial.registry().translationKey(), NamedTextColor.AQUA))
                    .build();
            layout.setItem(itemSlots[i], stack, this::handleClick);
        }

        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.PUSH_LAYER_VIEW));
        this.setLayout(layout);
    }

    /**
     * Handles the click on a push block item in the inventory.
     * @param player the player who clicked
     * @param slot the slot that was clicked
     * @param clickType the type of click (e.g., LEFT, RIGHT)
     * @param stack the item stack that was clicked
     * @param result the consumer to handle the click result
     */
    private void handleClick(@NotNull Player player, int slot, @NotNull Click clickType, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        EventDispatcher.call(new PlayerPushBlockSelectEvent(player, stack.material()));
    }
}
