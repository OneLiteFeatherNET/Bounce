package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.util.Components;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupMessages.DELETE_CLICK;
import static net.theevilreaper.bounce.setup.util.SetupMessages.TELEPORT_CLICK;

public class PositionSlot extends AbstractDataSlot {

    private static final DecimalFormat DECIMAL_FORMAT;

    static {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
        DECIMAL_FORMAT.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    }

    private final Pos position;

    public PositionSlot(@NotNull OverviewType overviewType, @Nullable Pos position) {
        super(overviewType);
        this.position = position;
    }

    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();

        if (position == null) {
            return overviewItem;
        }
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.addAll(Components.pointToLore(MiniMessage.miniMessage(), position, DECIMAL_FORMAT));
        lore.add(Component.empty());
        lore.add(TELEPORT_CLICK);
        lore.add(DELETE_CLICK);
        lore.add(Component.empty());

        return asBuilder(overviewItem).lore(lore).build();
    }

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull Click click, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());
        if ((!(click instanceof Click.Left || click instanceof Click.Right))) return;
        if (click instanceof Click.Left && position != null) {
            player.closeInventory();
            player.teleport(position);
        }
    }
}
