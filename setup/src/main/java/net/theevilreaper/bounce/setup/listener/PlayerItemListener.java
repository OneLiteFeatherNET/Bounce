package net.theevilreaper.bounce.setup.listener;

import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.onelitefeather.guira.functional.SetupDataGetter;
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
    private final OptionalSetupDataGetter saveFunction;

    public PlayerItemListener(
            @NotNull PlayerConsumer invOpener,
            @NotNull OptionalSetupDataGetter saveFunction
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

        Optional<SetupData> fetchedData = this.saveFunction.get(player.getUuid());
        if (fetchedData.isEmpty()) return;

        BounceData setupData = ((BounceData) fetchedData.get());

        if (itemId == OVERVIEW_FLAG) {
            setupData.openInventory();
            return;
        }

        if (itemId == 0x04) {
            setupData.openGroundLayerView();
            return;
        }

        setupData.save();
        player.getInventory().setItemStack(0x0, ItemStack.AIR);
    }
}
