package net.theevilreaper.bounce.setup.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code SetupInventorySwitchEvent} is triggered when a player switches to a specific inventory.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 0.1.0
 */
public class SetupInventorySwitchEvent implements PlayerEvent {

    private final Player player;
    private final SwitchTarget target;

    /**
     * Constructs a new {@code SetupInventorySwitchEvent}.
     *
     * @param player the player who switched the inventory
     * @param target the target inventory that was switched to
     */
    public SetupInventorySwitchEvent(@NotNull Player player, @NotNull SwitchTarget target) {
        this.player = player;
        this.target = target;
    }

    /**
     * Gets the target inventory that the player switched to.
     *
     * @return the target inventory
     */
    public @NotNull SwitchTarget getTarget() {
        return target;
    }

    /**
     * Gets the player who switched the inventory.
     *
     * @return the player
     */
    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }

    /**
     * Enum representing the possible target inventories for the setup.
     *
     * @author Joltra
     * @version 1.1.0
     * @since 0.1.0
     */
    public enum SwitchTarget {
        /**
         * Switch to the overview of ground blocks.
         */
        GROUND_BLOCKS_OVERVIEW,
        /**
         * Switch to the ground block view of the map.
         */
        GROUND_BLOCK_VIEW,
        /**
         * Switch to the ground layer view of the map.
         */
        GROUND_LAYER_VIEW,
        /**
         * Switch to the overview of push blocks.
         */
        PUSH_LAYER_VIEW,
        /**
         * Switch to the overview of push blocks.
         */
        PUSH_BLOCKS_OVERVIEW,
        /**
         * Switch to the overview of maps.
         */
        MAP_OVERVIEW,
    }
}
