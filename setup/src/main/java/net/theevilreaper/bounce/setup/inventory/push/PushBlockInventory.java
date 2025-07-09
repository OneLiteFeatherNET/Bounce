package net.theevilreaper.bounce.setup.inventory.push;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.GlobalInventoryBuilder;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.event.push.PlayerPushBlockSelectEvent;
import net.theevilreaper.bounce.setup.inventory.SetupBlocks;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
     * Handles the click event for selecting a ground block.
     *
     * @param player    the player who clicked
     * @param slot      the slot clicked
     * @param clickType the type of click
     * @param result    the result of the inventory condition
     */
    private void handleClick(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        EventDispatcher.call(new PlayerPushBlockSelectEvent(player, result.getClickedItem().material()));
    }
}
