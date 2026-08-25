package net.theevilreaper.bounce.setup.inventory.slot.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.bounce.setup.inventory.area.AreaViewType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Displays one corner of the ground area (see {@link AreaViewType#LEFT_AREA_CORNER}/{@link AreaViewType#RIGHT_AREA_CORNER})
 * and captures the player's current position for it on left-click.
 */
public final class AreaCornerSlot extends AbstractAreaSlot {

    private final @Nullable Vec position;
    private final Consumer<Player> onSet;

    public AreaCornerSlot(AreaViewType type, @Nullable Vec position, Consumer<Player> onSet) {
        super(type);
        this.position = position;
        this.onSet = onSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem() {
        ItemStack baseItem = this.type.getItem();
        if (position == null) {
            return asBuilder(baseItem).lore(
                    Component.empty(),
                    Component.text("Not set", NamedTextColor.RED),
                    Component.empty(),
                    Component.text("Left-click: set to your position", NamedTextColor.GRAY)
            ).build();
        }
        return asBuilder(baseItem).lore(
                Component.empty(),
                Component.text("X: " + position.x() + " Y: " + position.y() + " Z: " + position.z(), NamedTextColor.YELLOW),
                Component.empty(),
                Component.text("Left-click: set to your position", NamedTextColor.GRAY)
        ).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void click(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        if (click instanceof Click.Left) onSet.accept(player);
    }
}
