package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.EntityKnockbackEvent;
import io.github.togar2.pvp.feature.knockback.KnockbackSettings;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.powerup.PowerUpType;

import java.util.function.Consumer;

public class KnockbackListener implements Consumer<EntityKnockbackEvent> {

    private static final KnockbackSettings BOOSTED_SETTINGS = KnockbackSettings.builder()
            .horizontal(0.8)
            .vertical(0.5)
            .verticalLimit(0.5)
            .extraHorizontal(1.0)
            .build();

    @Override
    public void accept(EntityKnockbackEvent event) {
        if (!(event.getEntity() instanceof BouncePlayer bouncePlayer)) return;
        if (!(event.getAttacker() instanceof Player attacker)) return;
        if (bouncePlayer.hasPowerUp(PowerUpType.SHIELD)) {
            event.setCancelled(true);
            return;
        }
        bouncePlayer.setLastDamager(attacker);
        if (attacker instanceof BouncePlayer bounceAttacker && bounceAttacker.hasPowerUp(PowerUpType.KNOCKBACK_BOOST)) {
            event.setSettings(BOOSTED_SETTINGS);
        }
    }
}
