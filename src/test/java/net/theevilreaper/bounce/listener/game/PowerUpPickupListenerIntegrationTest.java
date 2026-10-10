package net.theevilreaper.bounce.listener.game;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.potion.PotionEffect;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.event.PlayerPowerUpPickupEvent;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.powerup.PowerUpType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PowerUpPickupListenerIntegrationTest {

    private final PowerUpPickupListener listener = new PowerUpPickupListener();

    @Test
    void testRegularPowerUpGetsActivatedForThePicker(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer picker = (BouncePlayer) env.createPlayer(instance, new Pos(0, 41, 0));
        picker.startRound();

        listener.accept(new PlayerPowerUpPickupEvent(picker, PowerUpType.SUPER_JUMP));

        assertTrue(picker.hasPowerUp(PowerUpType.SUPER_JUMP));
    }

    @Test
    void testBlooperBlindsOnlyRoundActiveOpponents(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer picker = (BouncePlayer) env.createPlayer(instance, new Pos(0, 41, 0));
        picker.startRound();
        BouncePlayer opponent = (BouncePlayer) env.createPlayer(instance, new Pos(3, 41, 0));
        opponent.startRound();
        BouncePlayer spectator = (BouncePlayer) env.createPlayer(instance, new Pos(6, 41, 0));

        listener.accept(new PlayerPowerUpPickupEvent(picker, PowerUpType.BLOOPER));

        assertTrue(opponent.hasEffect(PotionEffect.BLINDNESS), "Opponents must get inked");
        assertFalse(picker.hasEffect(PotionEffect.BLINDNESS), "The picker must not ink themselves");
        assertFalse(spectator.hasEffect(PotionEffect.BLINDNESS), "Players outside the round must not get inked");
        assertFalse(picker.hasPowerUp(PowerUpType.BLOOPER), "The blooper is instant and must not stay active");
    }
}
