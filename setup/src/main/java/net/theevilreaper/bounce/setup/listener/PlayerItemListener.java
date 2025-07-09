package net.theevilreaper.bounce.setup.listener;

import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.util.SetupItems.ITEM_TAG;
import static net.theevilreaper.bounce.setup.util.SetupItems.OVERVIEW_FLAG;
import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;

public final class PlayerItemListener implements Consumer<PlayerUseItemEvent> {

    private final PlayerConsumer invOpener;
    private final Function<UUID, Optional<BounceData>> saveFunction;

    public PlayerItemListener(
            @NotNull PlayerConsumer invOpener,
            @NotNull Function<UUID, Optional<BounceData>> saveFunction
    ) {
        this.invOpener = invOpener;
        this.saveFunction = saveFunction;
    }

    @Override
    public void accept(@NotNull PlayerUseItemEvent event) {
        Player player = event.getPlayer();

        ItemStack stack = event.getItemStack();
        if (!stack.hasTag(ITEM_TAG)) return;

        int itemId = stack.getTag(ITEM_TAG);

        if (itemId == 0x00) {
            this.invOpener.accept(player);
            return;
        }

        if (!player.hasTag(SETUP_TAG)) return;

        Optional<BounceData> fetchedData = this.saveFunction.apply(player.getUuid());
        if (fetchedData.isEmpty()) return;

       BounceData setupData = fetchedData.get();

        if (itemId == OVERVIEW_FLAG) {
            setupData.openInventory();
            return;
        }

        setupData.save();
        player.getInventory().setItemStack(0x0, ItemStack.AIR);
    }
}
