package net.theevilreaper.bounce.setup.listener.inventory;

import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.inventory.InventoryService;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.*;
import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;

public final class SetupInventorySwitchListener implements Consumer<SetupInventorySwitchEvent> {

    private final InventoryService inventoryService;
    private final Function<UUID, Optional<BounceData>> profileGetter;

    public SetupInventorySwitchListener(
            @NotNull InventoryService inventoryService,
            @NotNull Function<UUID, Optional<BounceData>> profileGetter
    ) {
        this.inventoryService = inventoryService;
        this.profileGetter = profileGetter;
    }

    @Override
    public void accept(@NotNull SetupInventorySwitchEvent event) {
        Player player = event.getPlayer();
        if (!player.hasTag(SETUP_TAG)) return;


        if (event.getTarget() == SwitchTarget.GROUND_BLOCKS_OVERVIEW) {
            this.inventoryService.openGroundBlockInventory(player);
            return;
        }

        if (event.getTarget() == SwitchTarget.PUSH_BLOCKS_OVERVIEW) {
            this.inventoryService.openPushBlockInventory(player);
            return;
        }

        Optional<BounceData> optionalData = this.profileGetter.apply(player.getUuid());

        if (optionalData.isEmpty()) {
            player.sendMessage(SetupMessages.SELECT_MAP_FIRST);
            return;
        }

        BounceData data = optionalData.get();

        if (event.getTarget() == SwitchTarget.MAP_OVERVIEW) {
            data.openInventory();
        }

        if (event.getTarget() == SwitchTarget.GROUND_LAYER_VIEW) {
            data.openGroundLayerView();
        }

        if (event.getTarget() == SwitchTarget.PUSH_LAYER_VIEW) {
            data.openPushValueInventory();
        }

        if (event.getTarget() == SwitchTarget.GROUND_BLOCK_VIEW) {
            data.openGroundBlockView();
        }
    }
}
