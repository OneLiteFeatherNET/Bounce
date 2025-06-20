package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.FinalAttackEvent;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AttackListener implements Consumer<FinalAttackEvent> {

    private final Supplier<Phase> phaseSupplier;

    public AttackListener(@NotNull Supplier<Phase> phaseSupplier) {
        this.phaseSupplier = phaseSupplier;
    }

    @Override
    public void accept(@NotNull FinalAttackEvent event) {
        Phase phase = phaseSupplier.get();

        if (phase instanceof PlayingPhase) {
            event.setBaseDamage(0f);
            return;
        }

        event.setCancelled(true);
    }
}
