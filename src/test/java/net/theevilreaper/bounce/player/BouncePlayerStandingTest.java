package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerStandingTest {

    private static BouncePlayer createPlayer(Env env, Instance instance) {
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, new Pos(0.5, 41, 0.5));
        player.startRound();
        return player;
    }

    @Test
    void testPlayerWhoReachedTheTiedScoreFirstRanksHigher(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer first = createPlayer(env, instance);
        BouncePlayer second = createPlayer(env, instance);

        first.addPoints(10);
        second.addPoints(10);

        assertTrue(first.compareStanding(second) > 0, "On a tie the player who reached the score first must win");
        assertTrue(second.compareStanding(first) < 0);
    }

    @Test
    void testDroppingToATiedScoreCountsAsReachingItLater(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer dropping = createPlayer(env, instance);
        BouncePlayer holding = createPlayer(env, instance);

        dropping.addPoints(15);
        holding.addPoints(10);
        dropping.removePoints(5);

        assertTrue(holding.compareStanding(dropping) > 0,
                "The player who already held the tied score must rank above the one who fell down to it");
    }
}
