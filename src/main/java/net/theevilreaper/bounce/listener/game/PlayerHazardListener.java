package net.theevilreaper.bounce.listener.game;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.theevilreaper.bounce.event.PlayerDeathBlockEvent;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.player.BouncePlayer;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Handles a player touching a deadly hazard, i.e. a {@link PlayerLavaEvent} or a {@link PlayerDeathBlockEvent}.
 * The player gets teleported back to the game spawn and the last damager, if still part of the round,
 * is credited with the kill.
 */
public final class PlayerHazardListener implements Consumer<PlayerEvent> {

    private final Supplier<Pos> spawnCoordinates;

    public PlayerHazardListener(Supplier<Pos> spawnCoordinates) {
        this.spawnCoordinates = spawnCoordinates;
    }

    @Override
    public void accept(PlayerEvent event) {
        if (!(event.getPlayer() instanceof BouncePlayer bouncePlayer) || !bouncePlayer.isRoundActive()) return;

        Player lastDamager = bouncePlayer.getLastDamager();
        bouncePlayer.resetDamager();
        bouncePlayer.teleport(spawnCoordinates.get());

        // A damager who left the round in the meantime must not be credited with the kill
        if (!(lastDamager instanceof BouncePlayer damagerPlayer) || !damagerPlayer.isRoundActive()) {
            bouncePlayer.addDeath();
            return;
        }

        bouncePlayer.removePoints(5);
        damagerPlayer.addPoints(10);
    }
}
