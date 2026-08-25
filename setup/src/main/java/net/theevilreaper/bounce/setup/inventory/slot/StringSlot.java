package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.map.BaseMapBuilder;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.event.map.PlayerDeletePromptEvent;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupMessages.DELETE_CLICK;
import static net.theevilreaper.bounce.setup.util.SetupMessages.NO_SPACE_SEPARATOR;

public class StringSlot extends AbstractDataSlot<OverviewType> {

    private final String data;

    public StringSlot(OverviewType overviewType, @Nullable String data) {
        super(overviewType);
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();

        if (data == null || data.equals(BaseMapBuilder.DEFAULT_NAME)) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void click(Player player, int slot, Click click, ItemStack stack, Consumer<ClickHolder> result) {
        result.accept(ClickHolder.cancelClick());

        if (data == null || data.isEmpty() || data.equals(BaseMapBuilder.DEFAULT_NAME)) {
            EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_NAME));
            return;
        }

        if (click instanceof Click.Right) {
            EventDispatcher.call(new PlayerDeletePromptEvent(player, OverviewType.NAME));
        }
    }
}
