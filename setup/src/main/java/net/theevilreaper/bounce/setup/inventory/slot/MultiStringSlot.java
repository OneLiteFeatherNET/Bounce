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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupMessages.DELETE_CLICK;
import static net.theevilreaper.bounce.setup.util.SetupMessages.NO_SPACE_SEPARATOR;

public class MultiStringSlot extends AbstractDataSlot<OverviewType> {

    private final List<String> data;

    public MultiStringSlot(OverviewType overviewType, List<String> data) {
        super(overviewType);
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();

        if (data.isEmpty()) {
            return overviewItem;
        }
        return asBuilder(overviewItem).lore(
                        Component.empty(),
                        NO_SPACE_SEPARATOR.append(Component.space()).append(Component.text(String.join(", ", data), type.getColor())),
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

        if (data.isEmpty()) {
            EventDispatcher.call(new PlayerDialogRequestEvent(player, PlayerDialogRequestEvent.Target.SETUP_REQUEST_AUTHOR));
            return;
        }

        if (click instanceof Click.Right) {
            EventDispatcher.call(new PlayerDeletePromptEvent(player, type));
        }
    }
}
