package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLavaEvent implements PlayerEvent {

    private final Player player;

    /**
     * Constructs a new PlayerLavaEvent for the specified player.
     *
     * @param player the player who is in lava
     */
    public PlayerLavaEvent(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
