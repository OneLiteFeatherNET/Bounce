package net.theevilreaper.bounce.timer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.xerus.api.phase.TickDirection;
import net.theevilreaper.xerus.api.phase.TimedPhase;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.function.IntConsumer;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayingPhase extends TimedPhase {

    private final IntConsumer timeUpdater;

    private BounceGameFinishEvent.Reason reason;

    public PlayingPhase(@NotNull IntConsumer timeUpdater) {
        super("GamePhase", ChronoUnit.SECONDS, 1);
        this.setTickDirection(TickDirection.DOWN);
        this.setCurrentTicks(300);

        this.timeUpdater = timeUpdater;
    }

    @Override
    public void onUpdate() {
        this.timeUpdater.accept(getCurrentTicks());
        switch (getCurrentTicks()) {
            case 10:
            case 3:
            case 2:
            case 1:
                broadcast(Component.text("Works"));
               // Bukkit.broadcastMessage(game.getPrefix() + "§cEs verbleiben noch §6" + getCurrentTime() + " §cSekunden");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onFinish() {
        EventDispatcher.call(new BounceGameFinishEvent(BounceGameFinishEvent.Reason.TIME_OVER));
    }

    @Override
    public void onSkip() {
        EventDispatcher.call(new BounceGameFinishEvent(reason));
    }



    private void broadcast(@NotNull Component component) {
        Audience.audience(getConnectionManager().getOnlinePlayers())
                .sendMessage(component);
    }

    public void handlePlayerCheck() {
        Collection<@NotNull Player> onlinePlayers = getConnectionManager().getOnlinePlayers();

        if (onlinePlayers.isEmpty()) {
            this.onSkip();
            this.reason = BounceGameFinishEvent.Reason.PLAYER_LEFT;
            return;
        }

        if (onlinePlayers.size() == 1) {
            setSkipping(true);
            this.onSkip();
            this.reason = BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT;
        }
    }
}