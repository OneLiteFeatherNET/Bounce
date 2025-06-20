package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ScoreUpdateListener implements Consumer<ScoreUpdateEvent> {

    private final BiConsumer<UUID, Integer> scoreUpdater;

    public ScoreUpdateListener(@NotNull BiConsumer<UUID, Integer> scoreUpdater) {
        this.scoreUpdater = scoreUpdater;
    }

    @Override
    public void accept(@NotNull ScoreUpdateEvent event) {
        this.scoreUpdater.accept(event.getPlayer().getUuid(), event.getPoints());
    }
}
