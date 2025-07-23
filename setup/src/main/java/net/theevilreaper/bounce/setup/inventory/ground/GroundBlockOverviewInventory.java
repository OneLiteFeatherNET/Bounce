package net.theevilreaper.bounce.setup.inventory.ground;

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
import net.theevilreaper.bounce.setup.event.ground.PlayerGroundBlockSelectEvent;
import net.theevilreaper.bounce.setup.inventory.SetupBlocks;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.*;
import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public class GroundBlockOverviewInventory extends GlobalInventoryBuilder {

    public GroundBlockOverviewInventory() {
        super(Component.text("Select ground block"), InventoryType.CHEST_6_ROW);

        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), DECORATION);
        layout.setItems(LayoutCalculator.fillRow(getType()), DECORATION);

        int[] slots = LayoutCalculator.quad(InventoryType.CHEST_1_ROW.getSize(), InventoryType.CHEST_5_ROW.getSize() - 1);

        Iterator<Material> iterator = SetupBlocks.ALLOWED_GROUND_BLOCKS.iterator();

        for (int i = 0; i < slots.length && iterator.hasNext(); i++) {
            Material currentMaterial = iterator.next();
            ItemStack stack = ItemStack.builder(currentMaterial)
                    .customName(Component.translatable(currentMaterial.registry().translationKey(), NamedTextColor.AQUA))
                    .build();
            layout.setItem(slots[i], stack, this::handleClick);
        }
        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.GROUND_BLOCK_VIEW));
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
        EventDispatcher.call(new PlayerGroundBlockSelectEvent(player, result.getClickedItem().material()));
    }
}
