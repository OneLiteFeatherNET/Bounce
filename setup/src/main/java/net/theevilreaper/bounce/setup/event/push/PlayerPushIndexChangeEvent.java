package net.theevilreaper.bounce.setup.event.push;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerPushIndexChangeEvent implements PlayerEvent {

    private final Player player;
    private final int index;

    /**
     * Constructs a new {@code PlayerPushIndexChangeEvent}.
     *
     * @param player the player who changed the push index
     * @param index  the new push index
     */
    public PlayerPushIndexChangeEvent(Player player, int index) {
        this.player = player;
        this.index = index;
    }

    /**
     * Gets the new push index.
     *
     * @return the new push index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the player who changed the push index.
     *
     * @return the player
     */
    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
