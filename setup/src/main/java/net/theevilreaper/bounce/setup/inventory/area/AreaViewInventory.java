package net.theevilreaper.bounce.setup.inventory.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public final class AreaViewInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Setup area");

    private static final int SHUFFLE_INTERVAL_SLOT = 10;
    private static final int POS1_SLOT = 11;
    private static final int POS2_SLOT = 13;
    private static final int CONFIRM_SLOT = 15;
    private static final int RESHUFFLE_PERCENTAGE_SLOT = 16;

    private final GameMapBuilder gameMapBuilder;
    private @Nullable Vec pos1;
    private @Nullable Vec pos2;

    public AreaViewInventory(Player player, GameMapBuilder gameMapBuilder) {
        super(TITLE, InventoryType.CHEST_3_ROW, player);
        this.gameMapBuilder = gameMapBuilder;

        Area existingArea = gameMapBuilder.getArea();
        if (existingArea != null) {
            this.pos1 = existingArea.min();
            this.pos2 = existingArea.max();
        }

        InventoryLayout layout = InventoryLayout.fromType(getType());
        layout.setItems(LayoutCalculator.quad(0, getType().getSize() - 1), DECORATION);
        layout.setItem(getType().getSize() - 1, new SwitchTargetSlot(SwitchTarget.MAP_OVERVIEW));
        this.setLayout(layout);

        this.setDataLayoutFunction(dataLayoutFunction -> {
            InventoryLayout dataLayout = dataLayoutFunction == null ? InventoryLayout.fromType(getType()) : dataLayoutFunction;
            dataLayout.blank(LayoutCalculator.from(SHUFFLE_INTERVAL_SLOT, POS1_SLOT, POS2_SLOT, CONFIRM_SLOT, RESHUFFLE_PERCENTAGE_SLOT));

            dataLayout.setItem(SHUFFLE_INTERVAL_SLOT, getShuffleIntervalItem(), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                EventDispatcher.call(new PlayerDialogRequestEvent(p, PlayerDialogRequestEvent.Target.SETUP_SHUFFLE_INTERVAL));
            });
            dataLayout.setItem(POS1_SLOT, getPosItem(AreaViewType.LEFT_AREA_CORNER, pos1), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) setPos1ToCurrentPosition(p);
            });
            dataLayout.setItem(POS2_SLOT, getPosItem(AreaViewType.RIGHT_AREA_CORNER, pos2), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) setPos2ToCurrentPosition(p);
            });
            dataLayout.setItem(CONFIRM_SLOT, getConfirmItem(), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) confirm(p);
            });
            dataLayout.setItem(RESHUFFLE_PERCENTAGE_SLOT, getReshufflePercentageItem(), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                EventDispatcher.call(new PlayerDialogRequestEvent(p, PlayerDialogRequestEvent.Target.SETUP_RESHUFFLE_PERCENTAGE));
            });

            return dataLayout;
        });
    }

    /**
     * Sets Pos1 to the player's current position and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos1ToCurrentPosition(Player player) {
        this.pos1 = toVec(player.getPosition());
        this.invalidateDataLayout();
    }

    /**
     * Sets Pos2 to the player's current position and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos2ToCurrentPosition(Player player) {
        this.pos2 = toVec(player.getPosition());
        this.invalidateDataLayout();
    }

    /**
     * Builds a {@link GroundArea} from the captured positions and the current ground block/push data, and stores
     * it on the {@link GameMapBuilder}. A no-op if either position is still unset.
     *
     * @param player the player to notify
     */
    public void confirm(Player player) {
        if (pos1 == null || pos2 == null) return;

        PushEntry groundEntry = gameMapBuilder.getGroundBlockEntry();
        Area area = new GroundArea(pos1, pos2, groundEntry.getBlock(), gameMapBuilder.getPushDataBuilder().build());
        gameMapBuilder.area(area);
        player.sendMessage(Component.text("Area saved.", NamedTextColor.GREEN));
    }

    private Vec toVec(Pos pos) {
        return new Vec(pos.x(), pos.y(), pos.z());
    }

    private ItemStack getPosItem(AreaViewType type, @Nullable Vec pos) {
        ItemStack.Builder builder = ItemStack.builder(type.getMaterial())
                .customName(Component.text(type.getName(), type.getColor()));
        if (pos == null) {
            return builder.lore(
                    Component.empty(),
                    Component.text("Not set", NamedTextColor.RED),
                    Component.empty(),
                    Component.text("Left-click: set to your position", NamedTextColor.GRAY)
            ).build();
        }
        return builder.lore(
                Component.empty(),
                Component.text("X: " + pos.x() + " Y: " + pos.y() + " Z: " + pos.z(), NamedTextColor.YELLOW),
                Component.empty(),
                Component.text("Left-click: set to your position", NamedTextColor.GRAY)
        ).build();
    }

    private ItemStack getConfirmItem() {
        boolean ready = pos1 != null && pos2 != null;
        return ItemStack.builder(ready ? Material.LIME_DYE : Material.GRAY_DYE)
                .customName(Component.text(ready ? "Confirm area" : "Set both positions first", ready ? NamedTextColor.GREEN : NamedTextColor.RED))
                .build();
    }

    private ItemStack getShuffleIntervalItem() {
        int ticks = gameMapBuilder.getShuffleIntervalTicks();
        double seconds = ticks / 20.0;
        return ItemStack.builder(Material.CLOCK)
                .customName(Component.text("Reshuffle Interval", NamedTextColor.LIGHT_PURPLE))
                .lore(
                        Component.empty(),
                        Component.text(String.format(java.util.Locale.ROOT, "%d ticks (%.1fs)", ticks, seconds), NamedTextColor.YELLOW),
                        Component.empty(),
                        Component.text("Click to edit", NamedTextColor.GRAY),
                        Component.empty()
                ).build();
    }

    private ItemStack getReshufflePercentageItem() {
        double percentage = gameMapBuilder.getReshufflePercentage() * 100.0;
        return ItemStack.builder(Material.TARGET)
                .customName(Component.text("Reshuffle Percentage", NamedTextColor.LIGHT_PURPLE))
                .lore(
                        Component.empty(),
                        Component.text(String.format(java.util.Locale.ROOT, "%.1f%%", percentage), NamedTextColor.YELLOW),
                        Component.empty(),
                        Component.text("Click to edit", NamedTextColor.GRAY),
                        Component.empty()
                ).build();
    }
}
