package net.theevilreaper.bounce.setup.inventory.push;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.LoreHelper;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;
import static net.theevilreaper.bounce.setup.util.SetupTags.PUSH_SLOT_INDEX;

public final class PushValueInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Push Value");

    private static final int BLOCK_SLOT = 11;
    private static final int WEIGHT_SLOT = 13;
    private static final int VALUE_SLOT = 15;

    private final GameMapBuilder gameMapBuilder;

    public PushValueInventory(Player player, GameMapBuilder mapBuilder) {
        super(TITLE, InventoryType.CHEST_3_ROW, player);
        this.gameMapBuilder = mapBuilder;

        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), DECORATION);
        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.GROUND_LAYER_VIEW));

        this.setLayout(layout);
    }

    public void updateLayout(int index) {
        if (index < 0 || index >= this.gameMapBuilder.getPushDataBuilder().getPushValues().size()) {
            throw new IndexOutOfBoundsException("Invalid push entry index: " + index);
        }
        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;

            dataLayout.blank(LayoutCalculator.from(BLOCK_SLOT, WEIGHT_SLOT, VALUE_SLOT));
            PushEntry pushEntry = this.gameMapBuilder.getPushDataBuilder().getPushValues().get(index);

            ItemStack stack = ItemStack.builder(pushEntry.getBlock().material())
                    .build();

            dataLayout.setItem(BLOCK_SLOT, stack, this::handleBlockClick);
            dataLayout.setItem(WEIGHT_SLOT, LoreHelper.getWeight(pushEntry), this::handleWeightButtonClick);
            dataLayout.setItem(VALUE_SLOT, LoreHelper.getPushValue(pushEntry), this::handlePushButtonClick);

            return dataLayout;
        });
        this.invalidateDataLayout();
    }

    // aves only recomputes the data layout on invalidate if the inventory is currently open; otherwise it
    // relies on the static-layout-valid flag to trigger a refresh on the next open() — but that flag is
    // already true after the first render, so a later reopen would silently keep showing stale data.
    @Override
    public void invalidateDataLayout() {
        invalidateLayout();
        super.invalidateDataLayout();
    }

    private void handleBlockClick(Player player, int slot, Click clickType, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        player.closeInventory();

        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.PUSH_BLOCKS_OVERVIEW));
    }

    private void handlePushButtonClick(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());

        if ((!(click instanceof Click.Left || click instanceof Click.Right))) return;

        int index = player.getTag(PUSH_SLOT_INDEX);
        player.setTag(PUSH_SLOT_INDEX, index);
        EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_BLOCK_BOUNCE));
    }

    private void handleWeightButtonClick(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        if ((!(click instanceof Click.Left || click instanceof Click.Right))) return;

        int index = player.getTag(PUSH_SLOT_INDEX);
        player.setTag(PUSH_SLOT_INDEX, index);
        EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_BLOCK_WEIGHT));
    }
}
