package net.theevilreaper.bounce.timer;

import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.FlexibleListener;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.xerus.api.phase.TickDirection;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
        env.createPlayer(instance);
        env.createPlayer(instance);

        // 2 players online. If 1 disconnects (2 - 1 = 1 remaining < 2), it should pause
        lobbyPhase.setPaused(false);
        lobbyPhase.checkStopCondition();
        assertTrue(lobbyPhase.isPaused(), "LobbyPhase should pause if remaining players < minPlayers");
        assertEquals(configuredTime, lobbyPhase.getCurrentTicks(), "LobbyPhase should reset ticks when paused");

        // Now add a 3rd player (3 online). If 1 disconnects (3 - 1 = 2 remaining >= 2), it should not pause
        env.createPlayer(instance);
        lobbyPhase.setPaused(false);
        lobbyPhase.checkStopCondition();
        assertFalse(lobbyPhase.isPaused(), "LobbyPhase should not pause if remaining players >= minPlayers");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckKeepsGameRunningWithMultiplePlayers(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        env.createPlayer(instance);
        env.createPlayer(instance);
        env.createPlayer(instance);

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 3 players online, 1 leaves -> remaining = 2 -> game should continue
        playingPhase.handlePlayerCheck();
        assertFalse(playingPhase.isSkipping(), "PlayingPhase should not skip when 2+ players remain");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckSkipsWhenOnePlayerRemains(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        env.createPlayer(instance);
        env.createPlayer(instance);

        FlexibleListener<BounceGameFinishEvent> listen = env.listen(BounceGameFinishEvent.class);
        listen.followup(event -> assertEquals(BounceGameFinishEvent.Reason.ONE_PLAYER_LEFT, event.getReason()));

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 2 players online, 1 leaves -> remaining = 1 -> ONE_PLAYER_LEFT
        playingPhase.handlePlayerCheck();
        assertTrue(playingPhase.isSkipping(), "PlayingPhase should skip when only 1 player remains");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayingPhasePlayerCheckSkipsWhenZeroPlayersRemain(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        env.createPlayer(instance);

        FlexibleListener<BounceGameFinishEvent> listen = env.listen(BounceGameFinishEvent.class);
        listen.followup(event -> assertEquals(BounceGameFinishEvent.Reason.PLAYER_LEFT, event.getReason()));

        PlayingPhase playingPhase = new PlayingPhase(time -> {}, () -> {});

        // 1 player online, 1 leaves -> remaining = 0 -> PLAYER_LEFT
        playingPhase.handlePlayerCheck();
        assertTrue(playingPhase.isSkipping(), "PlayingPhase should skip when 0 players remain");

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
