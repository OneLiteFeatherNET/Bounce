package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.FinalDamageEvent;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.registry.DynamicRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DamageListener implements Consumer<FinalDamageEvent> {

    public DamageListener() {
    }

    @Override
    public void accept(@NotNull FinalDamageEvent event) {
        if (event.getEntity().getEntityType() != EntityType.PLAYER) return;

        DynamicRegistry.Key<DamageType> type = event.getDamage().getType();
        Player originPlayer = (Player) event.getEntity();

        // TODO: Check in a future state why MinestomPVP doesn't handle LAVA damage correctly
       /* if (type == DamageType.LAVA) {
            System.out.printf("Player %s was damaged by lava%n", originPlayer.getUsername());
            player.setVelocityNoUpdate(vec -> Vec.ZERO);
            BounceProfile profile = profileFunction.apply(originPlayer);
            if (profile == null) return;
            profile.removePoints(5);
            EventDispatcher.call(new ScoreDeathUpdateEvent(profile.getPlayer()));
            if (profile.getLastDamager() != null) {
                BounceProfile damagerProfile = profileFunction.apply(profile.getLastDamager());
                damagerProfile.addPoints(10);
                profile.resetDamager();
            }
            this.spawnConsumer.accept(originPlayer);
        }*/

        if (type == DamageType.ON_FIRE || type == DamageType.IN_FIRE) {
            originPlayer.setFireTicks(0);
        }

        event.setInvulnerabilityTicks(100);
    }
}