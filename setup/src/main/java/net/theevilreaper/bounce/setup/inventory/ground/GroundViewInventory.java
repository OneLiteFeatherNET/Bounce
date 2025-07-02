package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.push.PushValueInventory;
import net.theevilreaper.bounce.setup.inventory.slot.EmptyPushSlot;
import net.theevilreaper.bounce.setup.inventory.slot.MaterialSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class GroundViewInventory extends PersonalInventoryBuilder {

    public static final Tag<Integer> PUSH_SLOT_INDEX = Tag.Integer("push_slot_index");

    private static final Component NO_DATA_ITEM = Component.text("No data", NamedTextColor.RED);
    private static final List<Component> NO_DATA_LORE = List.of(
            Component.empty(),
            miniMessage().deserialize("<gray>Please do a <green>left click <gray>to add data"),
            Component.empty()
    );

    private static final List<Component> PUSH_LORE = List.of(
            Component.empty(),
            SetupMessages.NO_SPACE_SEPARATOR.append(Component.space()).append(Component.text("Push block", NamedTextColor.GREEN)),
            Component.empty()
    );

    private final PushValueInventory[] pushValueInventories;
    private final GameMapBuilder gameMapBuilder;

    public GroundViewInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
        super(Component.text("Setup playing area"), InventoryType.CHEST_3_ROW, player);
        this.gameMapBuilder = gameMapBuilder;
        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), SetupItems.DECORATION);

        this.setLayout(layout);

        int[] slots = LayoutCalculator.from(12, 14, 16);
        this.pushValueInventories = new PushValueInventory[slots.length - 1];

        int[] orangeSlots = LayoutCalculator.fillColumn(InventoryType.CHEST_3_ROW, 2);
        ItemStack orangeStack = ItemStack.builder(Material.ORANGE_STAINED_GLASS_PANE).customName(Component.empty()).build();

        layout.setItems(orangeSlots, orangeStack);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(slots);

            dataLayout.setItem(10, new MaterialSlot(gameMapBuilder.getGroundBlock().registry().material()), this::handleGroundButton);
            List<PushEntry> pushEntries = gameMapBuilder.getPushDataBuilder().getPushValues();
            System.out.println("Push entries size: " + pushEntries.size());
            for (int index = 0; index < slots.length; index++) {
                if (index >= pushEntries.size()) {
                    EmptyPushSlot emptyPushSlot = new EmptyPushSlot(getNoDataItem(index));
                    dataLayout.setItem(slots[index], emptyPushSlot, this::handlePushButton);
                    continue;
                }
                PushEntry entry = pushEntries.get(index);
                System.out.println("Adding push entry: " + entry.getBlock().name() + " with value: " + entry.getValue());
                Block block = entry.getBlock();
                dataLayout.setItem(slots[index], new MaterialSlot(getSlotItem(block.registry().material(), index)), this::handlePushButton);
            }
            return dataLayout;
        });
    }

    private void handlePushButton(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();

        if (!result.getClickedItem().hasTag(PUSH_SLOT_INDEX)) return;

        int slotId = result.getClickedItem().getTag(PUSH_SLOT_INDEX);

        if (slotId < 0 || slotId > pushValueInventories.length) {
            player.sendMessage(Component.text("Invalid push slot index!", NamedTextColor.RED));
            return;
        }

        PushValueInventory pushValueInventory = pushValueInventories[slotId];
        System.out.println("Opening push value inventory for slot: " + slotId);
        System.out.println("Has push value inventory: " + (pushValueInventory != null));
        List<PushEntry> pushValues = gameMapBuilder.getPushDataBuilder().getPushValues();
        if (pushValueInventory == null) {
            PushEntry pushEntry;
            if (!pushValues.isEmpty() && slotId < pushValues.size()) {
                pushEntry = pushValues.get(slotId);
            } else {
                pushEntry = new PushEntry(Block.BARRIER, 0);
            }
            pushValueInventory = new PushValueInventory(player, this.gameMapBuilder, () -> pushEntry);
            pushValueInventories[slotId] = pushValueInventory;
            pushValueInventory.register();
        }

        pushValueInventory.open();
    }

    @Override
    public void unregister() {
        super.unregister();

        for (PushValueInventory inventory : pushValueInventories) {
            if (inventory != null) {
                inventory.unregister();
            }
        }
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

    private @NotNull ItemStack getNoDataItem(int slotId) {
        return ItemStack.builder(Material.BARRIER)
                .customName(NO_DATA_ITEM)
                .lore(NO_DATA_LORE)
                .set(PUSH_SLOT_INDEX, slotId)
                .build();
    }

    private @NotNull ItemStack getSlotItem(@NotNull Material material, int slotId) {
        return ItemStack.builder(material)
                .customName(Component.translatable(material.registry().translationKey(), NamedTextColor.AQUA))
                .lore(PUSH_LORE)
                .set(PUSH_SLOT_INDEX, slotId)
                .build();
    }
}
