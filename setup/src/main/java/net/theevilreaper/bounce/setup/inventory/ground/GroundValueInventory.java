package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
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

public class GroundValueInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Ground block");

    private static final int BLOCK_SLOT = 11;
    private static final int VALUE_SLOT = 15;

    private final PushEntry pushEntry;

    public GroundValueInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
        super(TITLE, InventoryType.CHEST_3_ROW, player);
        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), DECORATION);

        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.GROUND_LAYER_VIEW));

        this.setLayout(layout);
        this.pushEntry = gameMapBuilder.getGroundBlockEntry();

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(LayoutCalculator.from(BLOCK_SLOT, VALUE_SLOT));
            Material material = pushEntry.getBlock().registry().material();
            dataLayout.setItem(BLOCK_SLOT, ItemStack.builder(material).build(), this::handleBlockClick);
            dataLayout.setItem(VALUE_SLOT, LoreHelper.getPushValue(pushEntry), this::handlePushButtonClick);
            return dataLayout;
        });
    }

    private void handleBlockClick(@NotNull Player player, int slot, @NotNull Click clickType, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        player.closeInventory();
        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.GROUND_BLOCKS_OVERVIEW));
    }

    private void handlePushButtonClick(@NotNull Player player, int slot, @NotNull Click click, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        if ((!(click instanceof Click.Left || click instanceof Click.Right))) return;

        int oldValue = this.pushEntry.getValue();
        if (click instanceof Click.Left) {
            this.pushEntry.incrementValue();
        } else {
            this.pushEntry.decrementValue();
        }

        if (this.pushEntry.getValue() != oldValue) {
            this.invalidateDataLayout();
        }
    }
}
