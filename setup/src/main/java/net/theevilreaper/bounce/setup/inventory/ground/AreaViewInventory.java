package net.theevilreaper.bounce.setup.inventory.ground;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
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
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.slot.SwitchTargetSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.theevilreaper.bounce.setup.util.SetupItems.DECORATION;

public final class AreaViewInventory extends PersonalInventoryBuilder {

    private static final Component TITLE = Component.text("Setup area");

    private static final int POS1_SLOT = 11;
    private static final int POS2_SLOT = 13;
    private static final int CONFIRM_SLOT = 15;

    private final GameMapBuilder gameMapBuilder;
    private @Nullable Vec pos1;
    private @Nullable Vec pos2;

    public AreaViewInventory(@NotNull Player player, @NotNull GameMapBuilder gameMapBuilder) {
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
            dataLayout.blank(LayoutCalculator.from(POS1_SLOT, POS2_SLOT, CONFIRM_SLOT));

            dataLayout.setItem(POS1_SLOT, getPosItem("Pos1", pos1), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) setPos1ToCurrentPosition(p);
            });
            dataLayout.setItem(POS2_SLOT, getPosItem("Pos2", pos2), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) setPos2ToCurrentPosition(p);
            });
            dataLayout.setItem(CONFIRM_SLOT, getConfirmItem(), (p, slot, click, stack, result) -> {
                result.accept(ClickHolder.cancelClick());
                if (click instanceof Click.Left) confirm(p);
            });

            return dataLayout;
        });
    }

    /**
     * Sets Pos1 to the player's current position and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos1ToCurrentPosition(@NotNull Player player) {
        this.pos1 = toVec(player.getPosition());
        this.invalidateDataLayout();
    }

    /**
     * Sets Pos2 to the player's current position and refreshes the layout.
     *
     * @param player the player whose position is captured
     */
    public void setPos2ToCurrentPosition(@NotNull Player player) {
        this.pos2 = toVec(player.getPosition());
        this.invalidateDataLayout();
    }

    /**
     * Builds a {@link GroundArea} from the captured positions and the current ground block/push data, and stores
     * it on the {@link GameMapBuilder}. A no-op if either position is still unset.
     *
     * @param player the player to notify
     */
    public void confirm(@NotNull Player player) {
        if (pos1 == null || pos2 == null) return;

        PushEntry groundEntry = gameMapBuilder.getGroundBlockEntry();
        Area area = new GroundArea(pos1, pos2, groundEntry.getBlock(), gameMapBuilder.getPushDataBuilder().build());
        gameMapBuilder.area(area);
        player.sendMessage(Component.text("Area saved.", NamedTextColor.GREEN));
    }

    private @NotNull Vec toVec(@NotNull Pos pos) {
        return new Vec(pos.x(), pos.y(), pos.z());
    }

    private @NotNull ItemStack getPosItem(@NotNull String label, @Nullable Vec pos) {
        ItemStack.Builder builder = ItemStack.builder(Material.STICK)
                .customName(Component.text(label, NamedTextColor.AQUA));
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

    private @NotNull ItemStack getConfirmItem() {
        boolean ready = pos1 != null && pos2 != null;
        return ItemStack.builder(ready ? Material.LIME_DYE : Material.GRAY_DYE)
                .customName(Component.text(ready ? "Confirm area" : "Set both positions first", ready ? NamedTextColor.GREEN : NamedTextColor.RED))
                .build();
    }
}
