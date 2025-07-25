package net.theevilreaper.bounce.setup.event.map;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

public class PlayerDeletePromptEvent implements PlayerEvent {

    private final Player player;
    private final OverviewType type;

    public PlayerDeletePromptEvent(@NotNull Player player, @NotNull OverviewType type) {
        this.player = player;
        this.type = type;
    }

    /**
     * Gets the type of overview that triggered the delete prompt event.
     *
     * @return the type of overview
     */
    public @NotNull OverviewType getType() {
        return type;
    }

    /**
     * Gets the player who triggered the delete prompt event.
     *
     * @return the player who triggered the event
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
