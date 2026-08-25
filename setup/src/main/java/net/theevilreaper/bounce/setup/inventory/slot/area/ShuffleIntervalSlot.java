package net.theevilreaper.bounce.setup.inventory.slot.area;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.inventory.area.AreaViewType;
import net.theevilreaper.bounce.setup.inventory.slot.AbstractDataSlot;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;

public final class ShuffleIntervalSlot extends AbstractDataSlot<AreaViewType> {

    private final int shuffleIntervalTicks;

    public ShuffleIntervalSlot(AreaViewType type, int shuffleIntervalTicks) {
        super(type);
        this.shuffleIntervalTicks = shuffleIntervalTicks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem() {
        ItemStack baseItem = this.type.getItem();
        double seconds = shuffleIntervalTicks / 20.0;
        return asBuilder(baseItem).lore(
                Component.empty(),
                Component.text(String.format(Locale.ROOT, "%d ticks (%.1fs)", shuffleIntervalTicks, seconds), NamedTextColor.YELLOW),
                Component.empty(),
                SetupMessages.CLICK_TO_EDIT,
                Component.empty()
        ).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void click(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_SHUFFLE_INTERVAL));
    }
}
