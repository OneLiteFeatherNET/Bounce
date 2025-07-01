package net.theevilreaper.bounce.setup.listener.inventory;

import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.setup.BounceSetup;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.SetupInventorySwitchEvent;
import net.theevilreaper.bounce.setup.inventory.InventoryService;
import net.theevilreaper.bounce.setup.util.SetupMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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
        if (!player.hasTag(BounceSetup.SETUP_TAG)) return;

        if (event.getTarget() == SetupInventorySwitchEvent.SwitchTarget.GROUND_LAYER) {
            this.inventoryService.openGroundLayerInventory(player);
            return;
        }


        Optional<BounceData> optionalData = this.profileGetter.apply(player.getUuid());

        if (optionalData.isEmpty()) {
            player.sendMessage(SetupMessages.SELECT_MAP_FIRST);
            return;
        }

        BounceData data = optionalData.get();
        player.openInventory(data.getGroundViewInventory().getInventory());

    }
}
