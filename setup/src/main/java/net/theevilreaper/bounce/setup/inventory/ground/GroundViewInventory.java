package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.inventory.slot.EmptyPushSlot;
import net.theevilreaper.bounce.setup.inventory.slot.MaterialSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class GroundViewInventory extends PersonalInventoryBuilder {

    private final Supplier<VoidConsumer> viewSwitcher;

    public GroundViewInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder, @NotNull Supplier<VoidConsumer> viewSwitcher) {
        super(Component.text("Setup playing area"), InventoryType.CHEST_3_ROW, player);
        InventoryLayout layout = InventoryLayout.fromType(getType());
        this.viewSwitcher = viewSwitcher;

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

    private void handleGroundButton(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
        player.closeInventory();
        MinecraftServer.getSchedulerManager().scheduleNextTick(viewSwitcher.get()::apply);
    }
}
