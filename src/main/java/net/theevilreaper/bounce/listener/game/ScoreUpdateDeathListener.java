package net.theevilreaper.bounce.listener.game;

import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.event.ScoreDeathUpdateEvent;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ScoreUpdateDeathListener implements Consumer<ScoreDeathUpdateEvent> {

    private final Supplier<Phase> phaseSupplier;
    private final Function<Player, BounceProfile> profileFunction;

    public ScoreUpdateDeathListener(
            @NotNull Supplier<Phase> phaseSupplier,
            @NotNull Function<Player, BounceProfile> profileFunction
    ) {
        this.phaseSupplier = phaseSupplier;
        this.profileFunction = profileFunction;
    }

    @Override
    public void accept(@NotNull ScoreDeathUpdateEvent event) {
        Phase phase = phaseSupplier.get();

        if (!(phase instanceof PlayingPhase)) return;

        Player player = event.getPlayer();
        BounceProfile profile = profileFunction.apply(player);
        profile.removePoints(5);
    }
}
