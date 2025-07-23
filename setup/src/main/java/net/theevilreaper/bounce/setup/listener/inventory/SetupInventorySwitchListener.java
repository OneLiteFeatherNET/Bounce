package net.theevilreaper.bounce.setup.listener.inventory;

import net.minestom.server.entity.Player;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.inventory.InventoryService;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent.*;
import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;

public final class SetupInventorySwitchListener implements Consumer<SetupInventorySwitchEvent> {

    private final InventoryService inventoryService;
    private final OptionalSetupDataGetter profileGetter;

    public SetupInventorySwitchListener(
            @NotNull InventoryService inventoryService,
            @NotNull OptionalSetupDataGetter profileGetter
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

        Optional<SetupData> optionalData = this.profileGetter.get(player.getUuid());

        if (optionalData.isEmpty()) {
            player.sendMessage(SetupMessages.SELECT_MAP_FIRST);
            return;
        }

        BounceData data = ((BounceData) optionalData.get());

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
