package net.theevilreaper.bounce.setup.event.map;

import net.minestom.server.event.Event;
import net.onelitefeather.guira.data.SetupData;
import org.jetbrains.annotations.NotNull;

/**
 * Fired when a player discards an in-progress setup instead of saving it, e.g. after confirming a
 * {@link net.theevilreaper.bounce.setup.dialog.type.SaveValidationDialog}.
 */
public class SetupDiscardEvent implements Event {

    private final SetupData setupData;

    public SetupDiscardEvent(@NotNull SetupData setupData) {
        this.setupData = setupData;
    }

    /**
     * Returns the setup data of the discarded setup process.
     *
     * @return the setup data
     */
    public @NotNull SetupData getData() {
        return setupData;
    }
}
