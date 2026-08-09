package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.ScoreUpdateEvent;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class ScoreUpdateListener implements Consumer<ScoreUpdateEvent> {

    private final BiConsumer<UUID, Integer> scoreUpdater;

    public ScoreUpdateListener(BiConsumer<UUID, Integer> scoreUpdater) {
        this.scoreUpdater = scoreUpdater;
    }

    @Override
    public void accept(ScoreUpdateEvent event) {
        this.scoreUpdater.accept(event.getPlayer().getUuid(), event.getPoints());
    }
}
