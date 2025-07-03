package net.theevilreaper.bounce.setup.event.push;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code PlayerPushBlockSelectEvent} is triggered when a player selects a block for pushing.
 * This event is typically used to handle interactions with blocks that players can select for pushing in a game setup.
 *
 * @author Jotra
 * @version 1.0.0
 * @since 0.1.0
 */
public final class PlayerPushBlockSelectEvent implements PlayerEvent {

    private final Player player;
    private final Material material;

    /**
     * Constructs a new {@code PlayerPushBlockSelectEvent} with the specified player and material.
     *
     * @param player   the player who selected the block
     * @param material the material of the block that was selected
     */
    public PlayerPushBlockSelectEvent(@NotNull Player player, @NotNull Material material) {
        this.player = player;
        this.material = material;
    }

    /**
     * Gets the material of the block that was selected by the player.
     *
     * @return the material of the selected block
     */
    public @NotNull Material getMaterial() {
        return this.material;
    }

    /**
     * Gets the player who selected the block.
     *
     * @return the player involved in this event
     */
    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
