package net.theevilreaper.bounce.setup.inventory.area;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.kyori.adventure.text.Component;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import net.theevilreaper.bounce.setup.inventory.slot.area.AreaCornerSlot;
import net.theevilreaper.bounce.setup.inventory.slot.area.ReshufflePercentageSlot;
import net.theevilreaper.bounce.setup.inventory.slot.area.ShuffleIntervalSlot;
import org.jetbrains.annotations.Contract;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public final class AreaViewInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Setup area");
    private static final int[] SLOTS = new int[]{10, 12, 14, 16};

    private final GameMapBuilder gameMapBuilder;

    public AreaViewInventory(Player player, GameMapBuilder gameMapBuilder) {
        super(TITLE, InventoryType.CHEST_3_ROW, player);
        this.gameMapBuilder = gameMapBuilder;

        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), DECORATION);
        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.MAP_OVERVIEW));
        this.setLayout(layout);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(SLOTS);

            AreaViewType[] values = AreaViewType.values();

            for (int i = 0; i < values.length && i < SLOTS.length; i++) {
                AreaViewType type = values[i];
                dataLayout.setItem(SLOTS[i], getAreaSlot(type));
            }
            return dataLayout;
        });
    }

    /**
     * Maps a {@link AreaViewType} to a specific {@link ISlot}.
     *
     * @param type the {@link AreaViewType} to map
     * @return the corresponding {@link ISlot} for the given type
     */
    @Contract(value = "_ -> new", pure = true)
    private ISlot getAreaSlot(AreaViewType type) {
        return switch (type) {
            case LEFT_AREA_CORNER -> new AreaCornerSlot(AreaViewType.LEFT_AREA_CORNER, gameMapBuilder.getPos1(), this::setPos1ToCurrentPosition);
            case RIGHT_AREA_CORNER -> new AreaCornerSlot(AreaViewType.RIGHT_AREA_CORNER, gameMapBuilder.getPos2(), this::setPos2ToCurrentPosition);
            case SHUFFLE_INTERVAL -> new ShuffleIntervalSlot(AreaViewType.SHUFFLE_INTERVAL, gameMapBuilder.getShuffleIntervalTicks());
            case RESHUFFLE_PERCENTAGE ->  new ReshufflePercentageSlot(AreaViewType.RESHUFFLE_PERCENTAGE, gameMapBuilder.getReshufflePercentage());
        };
    }


    /**
     * Sets Pos1 on the {@link GameMapBuilder} to the player's current position, rebuilds the area once both
     * corners are known, and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos1ToCurrentPosition(Player player) {
        gameMapBuilder.pos1(toVec(player.getPosition()));
        rebuildAreaIfBothCornersSet();
        this.invalidateDataLayout();
    }

    /**
     * Sets Pos2 on the {@link GameMapBuilder} to the player's current position, rebuilds the area once both
     * corners are known, and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos2ToCurrentPosition(Player player) {
        gameMapBuilder.pos2(toVec(player.getPosition()));
        rebuildAreaIfBothCornersSet();
        this.invalidateDataLayout();
    }

    /**
     * Builds a {@link GroundArea} from the captured corners and the current ground block/push data, and stores
     * it on the {@link GameMapBuilder}. A no-op if either corner is still unset. Final validation that an area
     * is actually configured happens when the map itself is saved.
     */
    private void rebuildAreaIfBothCornersSet() {
        Vec pos1 = gameMapBuilder.getPos1();
        Vec pos2 = gameMapBuilder.getPos2();
        if (pos1 == null || pos2 == null) return;

        PushEntry groundEntry = gameMapBuilder.getGroundBlockEntry();
        Area area = new GroundArea(pos1, pos2, groundEntry.getBlock(), gameMapBuilder.getPushDataBuilder().build());
        gameMapBuilder.area(area);
    }

    private Vec toVec(Pos pos) {
        return new Vec(pos.x(), pos.y(), pos.z());
    }
}
