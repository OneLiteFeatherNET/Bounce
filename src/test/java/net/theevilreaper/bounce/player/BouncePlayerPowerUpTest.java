package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.potion.PotionEffect;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.powerup.PowerUpType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerPowerUpTest {

    private static BouncePlayer createPlayer(Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, new Pos(0.5, 41, 0.5));
        player.startRound();
        return player;
    }

    @Test
    void testPowerUpExpiresAfterItsDuration(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.activatePowerUp(PowerUpType.SHIELD);
        assertTrue(player.hasPowerUp(PowerUpType.SHIELD));
        assertFalse(player.hasPowerUp(PowerUpType.SUPER_JUMP));

        for (int i = 0; i < PowerUpType.SHIELD.getDurationTicks(); i++) {
            env.tick();
        }

        assertFalse(player.hasPowerUp(PowerUpType.SHIELD), "The power-up must expire after its duration");
    }

    @Test
    void testDoublePointsDoublesAwardedPoints(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.addPoints(10);
        player.activatePowerUp(PowerUpType.DOUBLE_POINTS);
        player.addPoints(10);

        assertEquals(30, player.getPoints());
    }

    @Test
    void testSpeedAppliesThePotionEffect(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.activatePowerUp(PowerUpType.SPEED);

        assertTrue(player.hasEffect(PotionEffect.SPEED));
    }

    @Test
    void testEndingTheRoundClearsPowerUps(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.activatePowerUp(PowerUpType.SPEED);
        player.activatePowerUp(PowerUpType.DOUBLE_POINTS);

        player.endRound();

        assertFalse(player.hasPowerUp(PowerUpType.SPEED));
        assertFalse(player.hasPowerUp(PowerUpType.DOUBLE_POINTS));
        assertFalse(player.hasEffect(PotionEffect.SPEED), "The speed effect must not outlive the round");
    }
}
