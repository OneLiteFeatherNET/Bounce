package net.theevilreaper.bounce.listener.game;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.player.BouncePlayer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerLavaListener implements Consumer<PlayerLavaEvent> {

    private final Supplier<Pos> spawnCoordinates;

    public PlayerLavaListener(Supplier<Pos> spawnCoordinates) {
        this.spawnCoordinates = spawnCoordinates;
    }

    @Override
    public void accept(PlayerLavaEvent event) {
        if (!(event.getPlayer() instanceof BouncePlayer bouncePlayer) || !bouncePlayer.isRoundActive()) return;

        Player lastDamager = bouncePlayer.getLastDamager();

        if (lastDamager == null) {
            bouncePlayer.addDeath();
            bouncePlayer.teleport(spawnCoordinates.get());
            return;
        }

        bouncePlayer.removePoints(5);
        bouncePlayer.teleport(spawnCoordinates.get());
        if (lastDamager instanceof BouncePlayer damagerPlayer) {
            damagerPlayer.addPoints(10);
        }
        bouncePlayer.resetDamager();
    }
}
