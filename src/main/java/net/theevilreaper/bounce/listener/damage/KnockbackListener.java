package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.EntityKnockbackEvent;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.player.BouncePlayer;

import java.util.function.Consumer;

public class KnockbackListener implements Consumer<EntityKnockbackEvent> {

    @Override
    public void accept(EntityKnockbackEvent event) {
        if (!(event.getEntity() instanceof BouncePlayer bouncePlayer)) return;
        if (!(event.getAttacker() instanceof Player attacker)) return;
        bouncePlayer.setLastDamager(attacker);
    }
}
