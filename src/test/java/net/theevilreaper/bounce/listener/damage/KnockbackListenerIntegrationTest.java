package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.EntityKnockbackEvent;
import io.github.togar2.pvp.feature.knockback.KnockbackSettings;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.powerup.PowerUpType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class KnockbackListenerIntegrationTest {

    private final KnockbackListener listener = new KnockbackListener();

    private static EntityKnockbackEvent createEvent(BouncePlayer victim, BouncePlayer attacker) {
        return new EntityKnockbackEvent(victim, attacker, EntityKnockbackEvent.KnockbackType.ATTACK, EntityKnockbackEvent.AnimationType.DIRECTIONAL);
    }

    @Test
    void testRegularKnockbackTracksTheDamager(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, new Pos(0, 41, 0));
        BouncePlayer attacker = (BouncePlayer) env.createPlayer(instance, new Pos(1, 41, 0));

        EntityKnockbackEvent event = createEvent(victim, attacker);
        listener.accept(event);

        assertFalse(event.isCancelled());
        assertSame(attacker, victim.getLastDamager());
        assertEquals(KnockbackSettings.DEFAULT, event.getSettings());
    }

    @Test
    void testShieldCancelsKnockback(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, new Pos(0, 41, 0));
        BouncePlayer attacker = (BouncePlayer) env.createPlayer(instance, new Pos(1, 41, 0));
        victim.activatePowerUp(PowerUpType.SHIELD);

        EntityKnockbackEvent event = createEvent(victim, attacker);
        listener.accept(event);

        assertTrue(event.isCancelled());
        assertNull(victim.getLastDamager(), "A shielded hit must not count as a damage source");
    }

    @Test
    void testKnockbackBoostIncreasesKnockback(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, new Pos(0, 41, 0));
        BouncePlayer attacker = (BouncePlayer) env.createPlayer(instance, new Pos(1, 41, 0));
        attacker.activatePowerUp(PowerUpType.KNOCKBACK_BOOST);

        EntityKnockbackEvent event = createEvent(victim, attacker);
        listener.accept(event);

        assertTrue(event.getSettings().horizontal() > KnockbackSettings.DEFAULT.horizontal());
    }
}
