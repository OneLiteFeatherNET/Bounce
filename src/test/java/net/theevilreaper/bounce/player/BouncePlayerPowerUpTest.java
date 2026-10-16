package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import net.minestom.server.potion.Potion;
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
    void testSlowFallingAppliesThePotionEffect(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.activatePowerUp(PowerUpType.SLOW_FALLING);

        assertTrue(player.hasEffect(PotionEffect.SLOW_FALLING));
    }

    @Test
    void testEndingTheRoundClearsPowerUps(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.activatePowerUp(PowerUpType.SLOW_FALLING);
        player.activatePowerUp(PowerUpType.DOUBLE_POINTS);

        player.endRound();

        assertFalse(player.hasPowerUp(PowerUpType.SLOW_FALLING));
        assertFalse(player.hasPowerUp(PowerUpType.DOUBLE_POINTS));
        assertFalse(player.hasEffect(PotionEffect.SLOW_FALLING), "The slow falling effect must not outlive the round");
    }

    @Test
    void testGravityFlipExpiresAfterItsDuration(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        double defaultGravity = player.getAttributeValue(Attribute.GRAVITY);
        player.flipGravity(PowerUpType.GRAVITY_FLIP.getDurationTicks());
        assertTrue(player.getAttributeValue(Attribute.GRAVITY) < 0);

        for (int i = 0; i < PowerUpType.GRAVITY_FLIP.getDurationTicks(); i++) {
            env.tick();
        }

        assertFalse(player.isGravityFlipped(), "The gravity flip must expire after its duration");
        assertEquals(defaultGravity, player.getAttributeValue(Attribute.GRAVITY));
    }

    @Test
    void testEndingTheRoundResetsGravity(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        double defaultGravity = player.getAttributeValue(Attribute.GRAVITY);
        player.flipGravity(PowerUpType.GRAVITY_FLIP.getDurationTicks());

        player.endRound();

        assertFalse(player.isGravityFlipped());
        assertEquals(defaultGravity, player.getAttributeValue(Attribute.GRAVITY), "The gravity flip must not outlive the round");
    }

    @Test
    void testEndingTheRoundRemovesBlooperBlindness(@NotNull Env env) {
        BouncePlayer player = createPlayer(env);
        player.addEffect(new Potion(PotionEffect.BLINDNESS, 0, PowerUpType.BLOOPER.getDurationTicks()));

        player.endRound();

        assertFalse(player.hasEffect(PotionEffect.BLINDNESS), "The blindness of a blooper must not outlive the round");
    }
}
