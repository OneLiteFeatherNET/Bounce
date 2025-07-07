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
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public class GroundValueInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Ground block");
    private static final Component DISPLAY_NAME = Component.text("Boost Value", NamedTextColor.GREEN);
    private static final Component CURRENT_VALUE = Component.text("Current:", NamedTextColor.GRAY).append(Component.space());
    private static final Component LEFT_CLICK = miniMessage().deserialize("<yellow>Left-click</yellow><gray>: </gray><green>+1 value</green>");
    private static final Component RIGHT_CLICK = miniMessage().deserialize("<yellow>Right-click</yellow><gray>: </gray><red>-1 value</red>");

    private static final int BLOCK_SLOT = 11;
    private static final int VALUE_SLOT = 15;

    private final PushEntry pushEntry;

    public GroundValueInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
        super(TITLE, InventoryType.CHEST_3_ROW, player);
        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), DECORATION);

        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.LAYER_OVERVIEW));

        this.setLayout(layout);
        this.pushEntry = gameMapBuilder.getGroundBlockEntry();
        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;


            dataLayout.blank(LayoutCalculator.from(BLOCK_SLOT, VALUE_SLOT));
            Material material = pushEntry.getBlock().registry().material();

            dataLayout.setItem(BLOCK_SLOT, ItemStack.builder(material).build(), this::handleBlockClick);
            dataLayout.setItem(VALUE_SLOT, getPushValue(), this::handlePushButtonClick);

            return dataLayout;
        });
    }

    private void handleBlockClick(@NotNull Player player, int i, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();
        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.GROUND_LAYER));
    }

    private void handlePushButtonClick(@NotNull Player player, int slot, @NotNull ClickType type, @NotNull InventoryConditionResult result) {
        result.setCancel(true);

        if (type != ClickType.LEFT_CLICK && type != ClickType.RIGHT_CLICK) {
            return;
        }

        int oldValue = this.pushEntry.getValue();
        if (type == ClickType.LEFT_CLICK) {
            this.pushEntry.incrementValue();
        } else {
            this.pushEntry.decrementValue();
        }

        if (this.pushEntry.getValue() != oldValue) {
            this.invalidateDataLayout();
        }
    }

    private @NotNull ItemStack getPushValue() {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(CURRENT_VALUE.append(Component.text(this.pushEntry.getValue(), NamedTextColor.YELLOW)));
        lore.add(Component.empty());
        lore.add(LEFT_CLICK);
        lore.add(RIGHT_CLICK);
        lore.add(Component.empty());
        return ItemStack.builder(Material.FEATHER)
                .customName(DISPLAY_NAME)
                .lore(lore)
                .build();
    }
}
