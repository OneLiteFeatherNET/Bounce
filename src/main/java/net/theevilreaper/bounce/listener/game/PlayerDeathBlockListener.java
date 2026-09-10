package net.theevilreaper.bounce.listener.game;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.event.PlayerDeathBlockEvent;
import net.theevilreaper.bounce.player.BouncePlayer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerDeathBlockListener implements Consumer<PlayerDeathBlockEvent> {

    private final Supplier<Pos> spawnCoordinates;

    public PlayerDeathBlockListener(Supplier<Pos> spawnCoordinates) {
        this.spawnCoordinates = spawnCoordinates;
    }

    @Override
    public void accept(PlayerDeathBlockEvent event) {
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
