package net.theevilreaper.bounce.setup.inventory.slot;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.theevilreaper.bounce.setup.util.SetupMessages.DELETE_CLICK;
import static net.theevilreaper.bounce.setup.util.SetupMessages.NO_SPACE_SEPARATOR;

public class MultiStringSlot extends AbstractDataSlot {

    private final List<String> data;

    public MultiStringSlot(@NotNull OverviewType overviewType, @Nullable List<String> data) {
        super(overviewType);
        this.data = data;
    }

    @Override
    public ItemStack getItem() {
        ItemStack overviewItem = this.type.getItem();

        if (data == null || data.isEmpty()) {
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

    @Override
    protected void click(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult result) {
        result.setCancel(true);
    }

}
