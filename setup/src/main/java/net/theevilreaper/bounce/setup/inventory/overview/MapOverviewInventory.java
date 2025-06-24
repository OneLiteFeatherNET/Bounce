package net.theevilreaper.bounce.setup.inventory.overview;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.inventory.slot.BackSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

public final class MapOverviewInventory extends PersonalInventoryBuilder {

    public MapOverviewInventory(@NotNull Player player, @NotNull PlayerConsumer backFunction) {
        super(Component.text("Data view"), InventoryType.CHEST_3_ROW, player);

        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.quad(0, getType().getSize()), SetupItems.DECORATION);
        layout.setItem(getType().getSize(), new BackSlot(), (player1, i, clickType, result) -> backFunction.accept(player));
        this.setLayout(layout);
    }
}
