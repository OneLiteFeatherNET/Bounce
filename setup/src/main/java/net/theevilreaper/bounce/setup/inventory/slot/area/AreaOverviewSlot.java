package net.theevilreaper.bounce.setup.inventory.slot.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.SwitchTarget;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import net.theevilreaper.bounce.setup.inventory.slot.AbstractDataSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class AreaOverviewSlot extends AbstractDataSlot {

    private final @Nullable Area area;

    public AreaOverviewSlot(OverviewType overviewType, @Nullable Area area) {
        super(overviewType);
        this.area = area;
    }

    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();
        if (area == null) {
            return asBuilder(overviewItem).lore(
                    Component.empty(),
                    Component.text("Not set", NamedTextColor.RED),
                    Component.empty(),
                    Component.text("Click to configure", NamedTextColor.GRAY),
                    Component.empty()
            ).build();
        }
        return asBuilder(overviewItem).lore(
                Component.empty(),
                Component.text("Configured", type.getColor()),
                Component.empty(),
                Component.text("Click to edit", NamedTextColor.GRAY),
                Component.empty()
        ).build();
    }

    @Override
    protected void click(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        EventDispatcher.call(new SetupInventorySwitchEvent(player, SwitchTarget.AREA_VIEW));
    }
}
