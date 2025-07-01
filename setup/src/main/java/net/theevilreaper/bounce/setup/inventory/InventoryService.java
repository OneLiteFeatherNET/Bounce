package net.theevilreaper.bounce.setup.inventory;

import net.minestom.server.entity.Player;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.setup.inventory.ground.GroundBlockInventory;
import net.theevilreaper.bounce.setup.inventory.push.PushBlockInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public final class InventoryService {

    private final MapSetupInventory mapSetupInventory;
    private final GroundBlockInventory groundBlockInventory;
    private final PushBlockInventory pushBlockInventory;

    public InventoryService(@NotNull Supplier<List<MapEntry>> entries) {
        this.mapSetupInventory = new MapSetupInventory(entries);

        this.groundBlockInventory = new GroundBlockInventory();
        this.pushBlockInventory = new PushBlockInventory();

        this.mapSetupInventory.register();
        this.groundBlockInventory.register();
        this.pushBlockInventory.register();
    }

    public void cleanup() {
        this.mapSetupInventory.unregister();
        this.groundBlockInventory.unregister();
        this.pushBlockInventory.unregister();
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
    public void openGroundBlockInventory(@NotNull Player player) {
        player.openInventory(this.groundBlockInventory.getInventory());
    }

    /**
     * Opens the push layer inventory for the specified player.
     *
     * @param player the player to open the inventory for
     */
    public void openPushBlockInventory(@NotNull Player player) {
        player.openInventory(this.pushBlockInventory.getInventory());
    }
}
