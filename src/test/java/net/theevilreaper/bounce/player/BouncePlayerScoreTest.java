package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerScoreTest {

    private static BouncePlayer createPlayer(Env env, Instance instance) {
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, new Pos(0.5, 41, 0.5));
        player.startRound();
        return player;
    }

    @Test
    void testRemovingMorePointsThanAvailableStopsAtZero(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        BouncePlayer player = createPlayer(env, env.createFlatInstance());
        player.addPoints(3);

        player.removePoints(5);

        assertEquals(0, player.getPoints(), "The score must never drop below zero");
    }
}
