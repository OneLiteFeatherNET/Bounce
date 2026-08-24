package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fills an {@link Area}'s scanned positions with a weighted-random mix of its {@link PushEntry} blocks, and
 * partially reshuffles that fill at runtime.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class AreaFiller {

    private AreaFiller() {
        // Prevent instantiation
    }

    /**
     * Scans the area (if not already scanned) and fills every found position with a weighted-random block.
     *
     * @param instance the instance to place blocks in
     * @param area     the area to fill
     */
    public static void fill(@NotNull Instance instance, @NotNull Area area) {
        area.calculatePositions(instance);

        List<PushEntry> entries = area.data().push();
        for (Vec position : area.positions()) {
            instance.setBlock(position, pickWeightedBlock(entries, area.groundBlock()));
        }
    }

    /**
     * Re-rolls {@code percentage} of the area's already scanned positions, skipping the position directly under
     * any of the given players so nobody's ground changes under their feet.
     *
     * @param instance   the instance to place blocks in
     * @param area       the area to reshuffle, must already have positions calculated (see {@link #fill})
     * @param percentage the fraction (0.0-1.0) of positions to re-roll
     * @param players    players whose current standing position must not be touched
     */
    public static void reshuffle(@NotNull Instance instance, @NotNull Area area, double percentage, @NotNull Collection<Player> players) {
        List<Vec> positions = area.positions();
        if (positions.isEmpty()) return;

        Set<Vec> excluded = new HashSet<>();
        for (Player player : players) {
            Pos playerPosition = player.getPosition();
            excluded.add(new Vec(Math.floor(playerPosition.x()), Math.floor(playerPosition.y() - 1), Math.floor(playerPosition.z())));
        }

        List<Vec> candidates = new ArrayList<>();
        for (Vec position : positions) {
            if (!excluded.contains(position)) candidates.add(position);
        }
        if (candidates.isEmpty()) return;

        Collections.shuffle(candidates, ThreadLocalRandom.current());
        int amount = Math.min(candidates.size(), (int) Math.round(positions.size() * percentage));

        List<PushEntry> entries = area.data().push();
        for (int i = 0; i < amount; i++) {
            Vec position = candidates.get(i);
            instance.setBlock(position, pickWeightedBlock(entries, area.groundBlock()));
        }
    }

    private static @NotNull Block pickWeightedBlock(@NotNull List<PushEntry> entries, @NotNull Block fallback) {
        double roll = ThreadLocalRandom.current().nextDouble(); // 0.0 to 1.0
        double cumulative = 0.0;
        for (PushEntry entry : entries) {
            if (entry.isGround()) continue;
            double p = Math.max(0.0, Math.min(1.0, entry.getWeight()));
            cumulative += p;
            if (roll < cumulative) {
                return entry.getBlock();
            }
        }
        return fallback;
    }
}
