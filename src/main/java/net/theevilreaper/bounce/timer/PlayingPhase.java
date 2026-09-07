package net.theevilreaper.bounce.timer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.xerus.api.phase.TickDirection;
import net.theevilreaper.xerus.api.phase.TimedPhase;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.function.IntConsumer;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayingPhase extends TimedPhase {

    private final IntConsumer timeUpdater;
    private final VoidConsumer startTrigger;
    private @Nullable BounceGameFinishEvent.Reason reason;

    public PlayingPhase(IntConsumer timeUpdater, VoidConsumer startTrigger) {
        super("GamePhase", ChronoUnit.SECONDS, 1);
        this.setTickDirection(TickDirection.DOWN);
        this.setCurrentTicks(300);

        this.timeUpdater = timeUpdater;
        this.startTrigger = startTrigger;
    }

    @Override
    public void onStart() {
        super.onStart();
        startTrigger.apply();
    }

    @Override
    public void onUpdate() {
        this.timeUpdater.accept(getCurrentTicks());
        switch (getCurrentTicks()) {
            case 10:
            case 3:
            case 2:
            case 1:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onFinish() {
        EventDispatcher.call(new BounceGameFinishEvent(reason != null ? reason : BounceGameFinishEvent.Reason.TIME_OVER));
    }

    private void broadcast(Component component) {
        Audience.audience(getConnectionManager().getOnlinePlayers())
                .sendMessage(component);
    }

    public void handlePlayerCheck() {
        // The player who is leaving has already been removed from getOnlinePlayers() by the time
        // PlayerDisconnectEvent fires (Player#remove/PlayerConnection#disconnect removes them
        // synchronously beforehand), so the returned count already reflects the remaining players.
        Collection<Player> onlinePlayers = getConnectionManager().getOnlinePlayers();
        int remaining = onlinePlayers.size();

        if (remaining == 0) {
            setSkipping(true);
            this.reason = BounceGameFinishEvent.Reason.PLAYER_LEFT;
            finish();
            return;
        }

        if (remaining == 1) {
            setSkipping(true);
            this.reason = BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT;
            finish();
        }
    }
}