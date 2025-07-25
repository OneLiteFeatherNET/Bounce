package net.theevilreaper.bounce.setup.inventory.push;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.LoreHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;
import static net.theevilreaper.bounce.setup.util.SetupTags.PUSH_SLOT_INDEX;

public final class PushValueInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Push Value");

    private static final int BLOCK_SLOT = 11;
    private static final int VALUE_SLOT = 15;

    private final GameMapBuilder gameMapBuilder;

    public PushValueInventory(@NotNull Player player, @NotNull GameMapBuilder mapBuilder) {
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

            dataLayout.blank(LayoutCalculator.from(BLOCK_SLOT, VALUE_SLOT));
            PushEntry pushEntry = this.gameMapBuilder.getPushDataBuilder().getPushValues().get(index);

            var stack = ItemStack.builder(pushEntry.getBlock().registry().material())
                    .build();

            dataLayout.setItem(BLOCK_SLOT, stack, this::handleBlockClick);
            dataLayout.setItem(VALUE_SLOT, LoreHelper.getPushValue(pushEntry), this::handlePushButtonClick);

            return dataLayout;
        });
        this.invalidateDataLayout();
    }

    private void handleBlockClick(@NotNull Player player, int slot, @NotNull Click clickType, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        player.closeInventory();

        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.PUSH_BLOCKS_OVERVIEW));
    }

    private void handlePushButtonClick(@NotNull Player player, int slot, @NotNull Click click, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());

        if ((!(click instanceof Click.Left || click instanceof Click.Right))) return;

        int index = player.getTag(PUSH_SLOT_INDEX);

        PushEntry pushEntry = this.gameMapBuilder.getPushDataBuilder().getPushValues().get(index);
        int oldValue = pushEntry.getValue();
        if (click instanceof Click.Left) {
            pushEntry.incrementValue();
        } else {
            pushEntry.decrementValue();
        }

        if (pushEntry.getValue() != oldValue) {
            this.invalidateDataLayout();
        }
    }
}
