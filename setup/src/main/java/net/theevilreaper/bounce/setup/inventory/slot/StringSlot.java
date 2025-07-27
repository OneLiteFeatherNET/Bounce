package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.event.map.PlayerDeletePromptEvent;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupMessages.DELETE_CLICK;
import static net.theevilreaper.bounce.setup.util.SetupMessages.NO_SPACE_SEPARATOR;

public class StringSlot extends AbstractDataSlot {

    private final String data;

    public StringSlot(@NotNull OverviewType overviewType, @Nullable String data) {
        super(overviewType);
        this.data = data;
    }

    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();

        if (data == null) {
            return overviewItem;
        }
        return asBuilder(overviewItem).lore(
                        Component.empty(),
                        NO_SPACE_SEPARATOR.append(Component.space()).append(Component.text(data, type.getColor())),
                        Component.empty(),
                        DELETE_CLICK,
                        Component.empty()
                )
                .build();
    }

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull Click click, @NotNull ItemStack stack, @NotNull Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());

        if (click instanceof Click.Right) {
            EventDispatcher.call(new PlayerDeletePromptEvent(player, OverviewType.NAME));
        }
    }
}
