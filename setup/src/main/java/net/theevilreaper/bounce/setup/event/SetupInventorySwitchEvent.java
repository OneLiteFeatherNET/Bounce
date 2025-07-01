package net.theevilreaper.bounce.setup.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code SetupInventorySwitchEvent} is triggered when a player switches to a specific inventory.
 *
 * @version 1.0.0
 * @since 0.1.0
 * @author Joltra
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
     * @version 1.0.0
     * @since 0.1.0
     * @author Joltra
     */
    public enum SwitchTarget {
        GROUND_LAYER,
        PUSH_LAYER,
        LAYER_OVERVIEW,
        MAP_OVERVIEW,
    }
}
