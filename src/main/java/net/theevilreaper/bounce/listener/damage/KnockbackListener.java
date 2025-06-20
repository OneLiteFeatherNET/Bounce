package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.EntityKnockbackEvent;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.profile.BounceProfile;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class KnockbackListener implements Consumer<EntityKnockbackEvent> {

    private final Function<UUID, BounceProfile> profileFunction;

    public KnockbackListener(@NotNull Function<UUID, BounceProfile> profileFunction) {
        this.profileFunction = profileFunction;
    }

    @Override
    public void accept(@NotNull EntityKnockbackEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getAttacker() instanceof Player)) return;
        BounceProfile profile = profileFunction.apply(event.getEntity().getUuid());
        profile.setLastDamager(((Player) event.getAttacker()));
    }
}
