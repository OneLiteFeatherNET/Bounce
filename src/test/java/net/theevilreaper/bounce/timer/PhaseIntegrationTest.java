package net.theevilreaper.bounce.timer;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.FlexibleListener;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.xerus.api.phase.TickDirection;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PhaseIntegrationTest {

    @Test
    void testLobbyPhaseInitialTicksAndStopCondition(@NotNull Env env) {
        int configuredTime = 45;
        int minPlayers = 2;
        LobbyPhase lobbyPhase = new LobbyPhase(minPlayers, configuredTime);

        assertEquals(configuredTime, lobbyPhase.getCurrentTicks(),
                "LobbyPhase should initialize with the configured lobbyPhaseTime");
        assertEquals(TickDirection.DOWN, lobbyPhase.getTickDirection());

        Instance instance = env.createFlatInstance();
        // The leaving player is already gone from getOnlinePlayers() by the time this runs
        // (see PlayingPhase's testPlayingPhasePlayerCheckReflectsRealDisconnectTiming), so only
        // the players still remaining after the leave are created here.
        env.createPlayer(instance);

        // 1 player remains -> below minPlayers -> should pause
        lobbyPhase.setPaused(false);
        lobbyPhase.checkStopCondition();
        assertTrue(lobbyPhase.isPaused(), "LobbyPhase should pause if remaining players < minPlayers");
        assertEquals(configuredTime, lobbyPhase.getCurrentTicks(), "LobbyPhase should reset ticks when paused");

        // Now a 2nd player remains -> meets minPlayers -> should not pause
        env.createPlayer(instance);
        lobbyPhase.setPaused(false);
        lobbyPhase.checkStopCondition();
        assertFalse(lobbyPhase.isPaused(), "LobbyPhase should not pause if remaining players >= minPlayers");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckKeepsGameRunningWithMultiplePlayers(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        // The leaving player is already gone from getOnlinePlayers() by the time this runs
        // (see testPlayingPhasePlayerCheckReflectsRealDisconnectTiming), so only the players
        // still remaining after the leave are created here.
        env.createPlayer(instance);
        env.createPlayer(instance);

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 2 players remain online -> game should continue
        playingPhase.handlePlayerCheck();
        assertFalse(playingPhase.isFinished(), "PlayingPhase should not finish when 2+ players remain");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckSkipsWhenOnePlayerRemains(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        env.createPlayer(instance);

        FlexibleListener<BounceGameFinishEvent> listen = env.listen(BounceGameFinishEvent.class);
        listen.followup(event -> assertEquals(BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT, event.getReason()));

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 1 player remains online -> ONE_PLAYER_LEFT
        playingPhase.handlePlayerCheck();
        assertTrue(playingPhase.isFinished(), "PlayingPhase should finish (and let the series advance) when only 1 player remains");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckSkipsWhenZeroPlayersRemain(@NotNull Env env) {
        Instance instance = env.createFlatInstance();

        FlexibleListener<BounceGameFinishEvent> listen = env.listen(BounceGameFinishEvent.class);
        listen.followup(event -> assertEquals(BounceGameFinishEvent.Reason.PLAYER_LEFT, event.getReason()));

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 0 players remain online -> PLAYER_LEFT
        playingPhase.handlePlayerCheck();
        assertTrue(playingPhase.isFinished(), "PlayingPhase should finish (and let the series advance) when 0 players remain");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckReflectsRealDisconnectTiming(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        env.createConnection().connect(instance);
        Player leaving = env.createConnection().connect(instance);

        FlexibleListener<BounceGameFinishEvent> listen = env.listen(BounceGameFinishEvent.class);
        listen.followup(event -> assertEquals(BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT, event.getReason(),
                "With 2 real players online, one real disconnect must leave exactly 1 player -> ONE_PLAYER_LEFT, not PLAYER_LEFT"));

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});
        // LinearPhaseSeries#startCurrentPhase() wires this callback to advance to the next phase
        // (RestartPhase in production, which eventually stops the server). If handlePlayerCheck()
        // never actually finishes the phase, this callback - and therefore the server stop - never
        // happens, even though the round visibly "ended" via the BounceGameFinishEvent.
        AtomicBoolean advancedToNextPhase = new AtomicBoolean(false);
        playingPhase.setFinishedCallback(() -> advancedToNextPhase.set(true));

        // Mirrors production net.minestom.server.network.player.PlayerConnection#disconnect():
        // it calls ConnectionManager#removePlayer(connection) synchronously and only *schedules*
        // Player#remove() (which fires PlayerDisconnectEvent) for the next tick. So by the time
        // our PlayerDisconnectEvent listener - and therefore handlePlayerCheck() - actually runs,
        // the leaving player is already gone from getOnlinePlayers().
        MinecraftServer.getConnectionManager().removePlayer(leaving.getPlayerConnection());
        assertEquals(1, MinecraftServer.getConnectionManager().getOnlinePlayers().size(),
                "The disconnecting player must already be gone from getOnlinePlayers() at this point");

        playingPhase.handlePlayerCheck();
        assertTrue(playingPhase.isFinished(), "PlayingPhase should finish when only 1 real player remains online");
        assertTrue(advancedToNextPhase.get(), "The phase series must be notified so it advances (and the server eventually stops)");

        env.destroyInstance(instance, true);
    }

    @Test
    void testRestartAndTeleportPhaseTickDirections() {
        RestartPhase restartPhase = new RestartPhase();
        assertEquals(TickDirection.DOWN, restartPhase.getTickDirection());

        TeleportPhase teleportPhase = new TeleportPhase(player -> {}, () -> {});
        assertEquals(TickDirection.DOWN, teleportPhase.getTickDirection());
    }
}
