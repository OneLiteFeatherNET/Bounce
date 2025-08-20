package net.theevilreaper.bounce.setup.event;

import net.minestom.server.event.Event;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStateNotifyEvent implements Event {

    protected final StateModel stateModel;

    protected AbstractStateNotifyEvent(@NotNull StateModel stateModel) {
        this.stateModel = stateModel;
    }

    protected @NotNull StateModel getStateModel() {
        return stateModel;
    }

    public interface StateModel {

    }

    public record GameMapBuilderState(@NotNull GameMapBuilder gameMapBuilder,
                                      @NotNull StateChange stateChange,
                                      @NotNull BounceData bounceData
    ) implements StateModel {

        public GameMapBuilderState(@NotNull BounceData bounceData, @NotNull StateChange stateChange) {
            this(bounceData.getMapBuilder(), stateChange, bounceData);
        }


        public enum StateChange {
            NAME, BUILDERS, SPAWN, GAME_SPAWN;
        }
    }

    public static final class GameMapBuilderStateNotifyEvent extends AbstractStateNotifyEvent {

        public GameMapBuilderStateNotifyEvent(@NotNull GameMapBuilderState gameMapBuilderState) {
            super(gameMapBuilderState);
        }

        @Override
        public @NotNull GameMapBuilderState getStateModel() {
            if (!(this.stateModel instanceof GameMapBuilderState gameMapBuilderState)) {
                throw new IllegalStateException("State model is not of type GameMapBuilderState");
            }
            return gameMapBuilderState;
        }
    }
}
