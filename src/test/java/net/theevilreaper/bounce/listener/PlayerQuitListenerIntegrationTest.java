package net.theevilreaper.bounce.listener;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.FlexibleListener;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.bounce.timer.RestartPhase;
import net.theevilreaper.bounce.timer.TeleportPhase;
import net.theevilreaper.xerus.api.phase.LinearPhaseSeries;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PlayerQuitListenerIntegrationTest {

    private static Player createPlayer(Env env, Instance instance) {
        Player player = env.createPlayer(instance);
        // Normally set by the PlayerJoinListener, the leave message needs it
        player.setDisplayName(Component.text(player.getUsername()));
        return player;
    }

    private static LinearPhaseSeries<Phase> createSeries() {
        LinearPhaseSeries<Phase> series = new LinearPhaseSeries<>("Game");
        series.add(new TeleportPhase(player -> {}, () -> {}));
        series.add(new PlayingPhase(300, time -> {}, () -> {}));
        series.add(new RestartPhase());
        series.start();
        return series;
    }

    @Test
    void testQuitDuringTeleportWithOnePlayerLeftEndsTheRound(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        // The leaving player is already gone from getOnlinePlayers() when the disconnect event fires,
        // so only the remaining player is online here
        Player remaining = createPlayer(env, instance);

        LinearPhaseSeries<Phase> series = createSeries();
        List<Player> leftPlayers = new ArrayList<>();
        PlayerQuitListener listener = new PlayerQuitListener(series::getCurrentPhase, leftPlayers::add);

        FlexibleListener<BounceGameFinishEvent> finishListener = env.listen(BounceGameFinishEvent.class);
        finishListener.followup(event -> assertEquals(BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT, event.getReason()));

        listener.accept(new PlayerDisconnectEvent(remaining));

        assertEquals(1, leftPlayers.size(), "The leave handling must run during the teleport phase");
        assertInstanceOf(RestartPhase.class, series.getCurrentPhase(), "The round must end when only one player remains");

        env.destroyInstance(instance, true);
    }

    @Test
    void testQuitDuringTeleportWithEnoughPlayersKeepsTheRound(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player leaving = createPlayer(env, instance);
        createPlayer(env, instance);
        createPlayer(env, instance);

        LinearPhaseSeries<Phase> series = createSeries();
        List<Player> leftPlayers = new ArrayList<>();
        PlayerQuitListener listener = new PlayerQuitListener(series::getCurrentPhase, leftPlayers::add);

        listener.accept(new PlayerDisconnectEvent(leaving));

        assertEquals(List.of(leaving), leftPlayers);
        assertInstanceOf(TeleportPhase.class, series.getCurrentPhase(), "The round must continue with enough players left");

        env.destroyInstance(instance, true);
    }
}
