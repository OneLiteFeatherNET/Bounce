package net.theevilreaper.bounce.event;

import net.theevilreaper.xerus.api.event.GameFinishEvent;
import org.jetbrains.annotations.NotNull;

public class BounceGameFinishEvent extends GameFinishEvent<BounceGameFinishEvent.Reason> {

    public BounceGameFinishEvent(@NotNull Reason reason) {
        super(reason);
    }

    public enum Reason {

        TIME_OVER,
        ONE_PLAYER_LEFT,
        PLAYER_LEFT,
    }
}
