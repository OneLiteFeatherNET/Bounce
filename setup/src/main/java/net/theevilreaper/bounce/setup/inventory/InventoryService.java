package net.theevilreaper.bounce.setup.inventory;

import net.minestom.server.entity.Player;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.setup.inventory.ground.GroundLayerInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public final class InventoryService {

    private final MapSetupInventory mapSetupInventory;
    private final GroundLayerInventory groundLayerInventory;

    public InventoryService(@NotNull Supplier<List<MapEntry>> entries) {
        this.mapSetupInventory = new MapSetupInventory(entries);

        this.groundLayerInventory = new GroundLayerInventory(() -> {
        });

        this.mapSetupInventory.register();
        this.groundLayerInventory.register();
    }

    public void cleanup() {
        this.mapSetupInventory.unregister();
        this.groundLayerInventory.unregister();
    }

    /**
     * Opens the map setup inventory for the specified player.
     *
     * @param player the player to open the inventory for
     */
    public void openMapSetupInventory(@NotNull Player player) {
        player.openInventory(this.mapSetupInventory.getInventory());
    }

    /**
     * Opens the ground layer inventory for the specified player.
     *
     * @param player the player to open the inventory for
     */
    public void openGroundLayerInventory(@NotNull Player player) {
        player.openInventory(this.groundLayerInventory.getInventory());
    }
}
