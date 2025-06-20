package net.theevilreaper.bounce.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ScoreUpdateEvent implements PlayerEvent {

    private final Player player;
    private final int points;

    public ScoreUpdateEvent(Player player, int points) {
        this.player = player;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
