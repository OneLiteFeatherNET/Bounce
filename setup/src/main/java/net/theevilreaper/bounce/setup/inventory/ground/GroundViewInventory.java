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
import net.theevilreaper.bounce.setup.event.push.PlayerPushIndexChangeEvent;
import net.theevilreaper.bounce.setup.inventory.slot.MaterialSlot;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.theevilreaper.bounce.setup.util.SetupTags.PUSH_SLOT_INDEX;

public class GroundViewInventory extends PersonalInventoryBuilder {

    private static final int[] PUSH_SLOTS = LayoutCalculator.from(12, 14, 16);
    private static final List<Component> PUSH_LORE = List.of(
            Component.empty(),
            SetupMessages.NO_SPACE_SEPARATOR.append(Component.space()).append(Component.text("Push block", NamedTextColor.GREEN)),
            Component.empty()
    );

    private GroundValueInventory groundValueInventory;
    private final GameMapBuilder gameMapBuilder;

    public GroundViewInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
        super(Component.text("Setup playing area"), InventoryType.CHEST_3_ROW, player);
        this.gameMapBuilder = gameMapBuilder;
        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), SetupItems.DECORATION);
        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SetupInventorySwitchEvent.SwitchTarget.MAP_OVERVIEW));
        this.setLayout(layout);

        int[] orangeSlots = LayoutCalculator.fillColumn(InventoryType.CHEST_3_ROW, 2);
        ItemStack orangeStack = ItemStack.builder(Material.ORANGE_STAINED_GLASS_PANE).customName(Component.empty()).build();

        layout.setItems(orangeSlots, orangeStack);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(PUSH_SLOTS);

            dataLayout.setItem(10, new MaterialSlot(gameMapBuilder.getGroundBlockEntry().getBlock().registry().material()), this::handleGroundButton);
            List<PushEntry> pushEntries = gameMapBuilder.getPushDataBuilder().getPushValues();

            for (int i = 1; i < pushEntries.size(); i++) {
                int inventorySlot = PUSH_SLOTS[i - 1];
                PushEntry pushEntry = pushEntries.get(i);
                Material material = pushEntry.getBlock().registry().material();
                ItemStack itemStack = getSlotItem(material, i);
                dataLayout.setItem(inventorySlot, new MaterialSlot(itemStack), this::handlePushButton);
            }

            return dataLayout;
        });
    }

    private void handlePushButton(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();

        if (!result.getClickedItem().hasTag(PUSH_SLOT_INDEX)) return;

        int pushIndex = result.getClickedItem().getTag(PUSH_SLOT_INDEX);

        EventDispatcher.call(new PlayerPushIndexChangeEvent(player, pushIndex));
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

        if (groundValueInventory == null) {
            groundValueInventory = new GroundValueInventory(player, gameMapBuilder);
            groundValueInventory.register();
        }
        groundValueInventory.open();
    }

    private @NotNull ItemStack getSlotItem(@NotNull Material material, int slotId) {
        return ItemStack.builder(material)
                .customName(Component.translatable(material.registry().translationKey(), NamedTextColor.AQUA))
                .lore(PUSH_LORE)
                .set(PUSH_SLOT_INDEX, slotId)
                .build();
    }

    public void invalidateGroundValueInventory() {
        if (groundValueInventory != null) {
            groundValueInventory.invalidateDataLayout();
        }
    }

    /**
     * Opens the ground value inventory for the player.
     * This method checks if the ground value inventory is initialized before attempting to open it.
     */
    public void openGroundBlockValueInventory() {
        if (groundValueInventory == null) return;
        this.groundValueInventory.open();
    }
}
