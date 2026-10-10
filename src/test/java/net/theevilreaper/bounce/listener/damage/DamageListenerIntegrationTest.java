package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.FinalDamageEvent;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MicrotusExtension.class)
class DamageListenerIntegrationTest {

    @Test
    void testHitPlayerIsInvulnerableForOneSecond(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player victim = env.createPlayer(instance);
        Player attacker = env.createPlayer(instance);

        FinalDamageEvent event = new FinalDamageEvent(victim, Damage.fromPlayer(attacker, 0f), 10, true);
        new DamageListener().accept(event);

        assertEquals(DamageListener.INVULNERABILITY_TICKS, event.getInvulnerabilityTicks());
        assertEquals(20, event.getInvulnerabilityTicks(), "A hit player should be protected for one second");

        env.destroyInstance(instance, true);
    }
}
