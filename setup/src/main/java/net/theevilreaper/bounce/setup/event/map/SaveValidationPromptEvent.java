package net.theevilreaper.bounce.setup.event.map;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Fired when a player tries to save a map which is still missing required data.
 */
public class SaveValidationPromptEvent implements PlayerEvent {

    private final Player player;
    private final List<String> missingFields;

    public SaveValidationPromptEvent(@NotNull Player player, @NotNull List<String> missingFields) {
        this.player = player;
        this.missingFields = missingFields;
    }

    /**
     * Gets the names of the required fields which are still missing.
     *
     * @return the missing field names
     */
    public @NotNull List<String> getMissingFields() {
        return missingFields;
    }

    /**
     * Gets the player who triggered the save validation prompt event.
     *
     * @return the player who triggered the event
     */
    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
