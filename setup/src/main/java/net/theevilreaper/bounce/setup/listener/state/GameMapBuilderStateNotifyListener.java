package net.theevilreaper.bounce.setup.listener.state;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.event.AbstractStateNotifyEvent.*;

public class GameMapBuilderStateNotifyListener implements Consumer<GameMapBuilderStateNotifyEvent> {

    @Override
    public void accept(@NotNull GameMapBuilderStateNotifyEvent event) {
        event.getStateModel().bounceData().triggerUpdate();
    }
}
