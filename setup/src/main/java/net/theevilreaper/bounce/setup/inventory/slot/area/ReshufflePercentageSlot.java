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
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;

public final class ReshufflePercentageSlot extends AbstractAreaSlot {

    private final double reshufflePercentage;

    public ReshufflePercentageSlot(AreaViewType type, double reshufflePercentage) {
        super(type);
        this.reshufflePercentage = reshufflePercentage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem() {
        ItemStack baseItem = this.type.getItem();
        double percentage = reshufflePercentage * 100.0;
        return asBuilder(baseItem).lore(
                Component.empty(),
                Component.text(String.format(Locale.ROOT, "%.1f%%", percentage), NamedTextColor.YELLOW),
                Component.empty(),
                CLICK_TO_EDIT,
                Component.empty()
        ).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void click(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_RESHUFFLE_PERCENTAGE));
    }
}
