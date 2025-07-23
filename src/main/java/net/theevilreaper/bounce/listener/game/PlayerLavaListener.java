package net.theevilreaper.bounce.listener.game;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.profile.BounceProfile;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PlayerLavaListener implements Consumer<PlayerLavaEvent> {

    private final Function<Player, BounceProfile> profileFunction;
    private final Supplier<Pos> spawnCoordinates;

    public PlayerLavaListener(@NotNull Function<Player, BounceProfile> profileFunction, @NotNull Supplier<Pos> spawnCoordinates) {
        this.profileFunction = profileFunction;
        this.spawnCoordinates = spawnCoordinates;
    }

    @Override
    public void accept(@NotNull PlayerLavaEvent event) {
        Player player = event.getPlayer();

        BounceProfile profile = profileFunction.apply(player);

        if (profile == null) return;

        if (profile.getLastDamager() == null) {
            profile.addDeath();
            player.teleport(spawnCoordinates.get());
            return;
        }

        profile.removePoints(5);
        player.teleport(spawnCoordinates.get());
        if (profile.getLastDamager() != null) {
            BounceProfile damagerProfile = profileFunction.apply(profile.getLastDamager());
            damagerProfile.addPoints(10);
            profile.resetDamager();
        }
    }
}
