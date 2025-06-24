package net.theevilreaper.bounce.setup.inventory.overview;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.theevilreaper.aves.inventory.InventoryLayout;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.inventory.slot.GroundSlot;
import net.theevilreaper.bounce.setup.inventory.slot.MultiStringSlot;
import net.theevilreaper.bounce.setup.inventory.slot.PositionSlot;
import net.theevilreaper.bounce.setup.inventory.slot.StringSlot;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

public final class MapOverviewInventory extends PersonalInventoryBuilder {

    private static final int[] DATA_SLOT = LayoutCalculator.from(9, 11, 13, 15, 17);

    private final GameMapBuilder builder;
    private final VoidConsumer viewSwitcher;

    public MapOverviewInventory(@NotNull Player player, @NotNull GameMapBuilder builder, @NotNull VoidConsumer viewSwitcher) {
        super(Component.text("Data view"), InventoryType.CHEST_3_ROW, player);
        this.builder = builder;
        this.viewSwitcher = viewSwitcher;
        InventoryLayout layout = InventoryLayout.fromType(getType());

        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), SetupItems.DECORATION);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(DATA_SLOT);

            OverviewType[] overviewTypes = OverviewType.getValues();

            for (int i = 0; i < overviewTypes.length && i < DATA_SLOT.length; i++) {
                OverviewType currentType = overviewTypes[i];
                dataLayout.setItem(DATA_SLOT[i], getOverViewItem(currentType));
            }
            return dataLayout;
        });
        this.setLayout(layout);
    }

    private @NotNull ISlot getOverViewItem(@NotNull OverviewType type) {
        return switch (type) {
            case SPAWN -> new PositionSlot(type, this.builder.getSpawn());
            case GAME_SPAWN -> new PositionSlot(type, this.builder.getGameSpawn());
            case GROUND -> new GroundSlot(type, viewSwitcher);
            case NAME -> new StringSlot(type, builder.getName());
            case BUILDER -> new MultiStringSlot(type, builder.getAuthors());
        };
    }
}
