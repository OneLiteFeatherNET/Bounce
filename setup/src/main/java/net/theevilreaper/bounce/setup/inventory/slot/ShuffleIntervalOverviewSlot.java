package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class ShuffleIntervalOverviewSlot extends AbstractDataSlot {

    private final int shuffleIntervalTicks;

    public ShuffleIntervalOverviewSlot(@NotNull OverviewType overviewType, int shuffleIntervalTicks) {
        super(overviewType);
        this.shuffleIntervalTicks = shuffleIntervalTicks;
    }

    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();
        double seconds = shuffleIntervalTicks / 20.0;
        return asBuilder(overviewItem).lore(
                Component.empty(),
                Component.text(String.format(java.util.Locale.ROOT, "%d ticks (%.1fs)", shuffleIntervalTicks, seconds), NamedTextColor.YELLOW),
                Component.empty(),
                Component.text("Click to edit", NamedTextColor.GRAY),
                Component.empty()
        ).build();
    }

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull Click click, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_SHUFFLE_INTERVAL));
    }
}
