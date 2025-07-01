package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.EmptyPushSlot;
import net.theevilreaper.bounce.setup.inventory.slot.MaterialSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

public class GroundViewInventory extends PersonalInventoryBuilder {

    public GroundViewInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
        super(Component.text("Setup playing area"), InventoryType.CHEST_3_ROW, player);
        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), SetupItems.DECORATION);

        this.setLayout(layout);

        int[] slots = LayoutCalculator.from(9, 11, 13, 15, 17);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(slots);

            int index = 0;
            dataLayout.setItem(slots[index++], new MaterialSlot(gameMapBuilder.getGroundBlock().registry().material()), this::handleGroundButton);

            Iterator<Map.Entry<Block, Double>> iterator = gameMapBuilder.getPushDataBuilder().getPushValues().entrySet().iterator();
            EmptyPushSlot emptyPushSlot = new EmptyPushSlot();
            for (; index < slots.length; index++) {
                if (!iterator.hasNext()) {
                    dataLayout.setItem(slots[index], emptyPushSlot);
                    continue;
                }
                Map.Entry<Block, Double> entry = iterator.next();
                dataLayout.setItem(slots[index], new MaterialSlot(entry.getKey().registry().material()));
            }
            return dataLayout;
        });
    }

    /**
     * Handles the button click for the ground layer inventory.
     *
     * @param player    the player who clicked the button
     * @param slot      the slot that was clicked
     * @param clickType the type of click that occurred
     * @param result    the result of the click action, which can be modified to cancel the action
     */
    private void handleGroundButton(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();
        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.GROUND_LAYER));
    }
}
