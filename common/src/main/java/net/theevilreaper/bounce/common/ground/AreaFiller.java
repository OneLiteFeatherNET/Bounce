package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.Nullable;

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

    /**
     * The chance a freshly filled position becomes a {@link Block#REDSTONE_BLOCK} death block instead of a
     * regular ground/push block. Death blocks are static: once placed by {@link #fill}, {@link #reshuffle} never
     * re-rolls them away and never introduces new ones.
     */
    private static final double DEATH_BLOCK_WEIGHT = 0.03;

    /**
     * Horizontal radius (in blocks, XZ only) around a given spawn point within which {@link #fill} will never
     * place a death block, so players don't land on (or immediately beside) one right after spawning.
     */
    private static final double SPAWN_EXCLUSION_RADIUS = 5.0;

    private AreaFiller() {
        // Prevent instantiation
    }

    /**
     * Scans the area (if not already scanned) and fills every found position with a weighted-random block, mixing
     * in static {@link Block#REDSTONE_BLOCK} death blocks at {@link #DEATH_BLOCK_WEIGHT}.
     *
     * @param instance the instance to place blocks in
     * @param area     the area to fill
     */
    public static void fill(Instance instance, Area area) {
        fill(instance, area, null);
    }

    /**
     * Scans the area (if not already scanned) and fills every found position with a weighted-random block, mixing
     * in static {@link Block#REDSTONE_BLOCK} death blocks at {@link #DEATH_BLOCK_WEIGHT}. No death block is ever
     * placed within {@link #SPAWN_EXCLUSION_RADIUS} blocks of {@code spawn}.
     *
     * @param instance the instance to place blocks in
     * @param area     the area to fill
     * @param spawn    the spawn point to keep clear of death blocks, or {@code null} to skip that check
     */
    public static void fill(Instance instance, Area area, @Nullable Point spawn) {
        area.calculatePositions(instance);

        List<PushEntry> entries = area.data().push();
        for (Vec position : area.positions()) {
            boolean nearSpawn = spawn != null && isWithinRadius(position, spawn, SPAWN_EXCLUSION_RADIUS);
            instance.setBlock(position, pickWeightedBlock(entries, area.groundBlock(), !nearSpawn));
        }
    }

    /**
     * Re-rolls {@code percentage} of the area's already scanned positions, skipping the position directly under
     * any of the given players so nobody's ground changes under their feet. Positions already holding a
     * {@link Block#REDSTONE_BLOCK} death block are left alone, and reshuffling never creates new ones.
     *
     * @param instance   the instance to place blocks in
     * @param area       the area to reshuffle, must already have positions calculated (see {@link #fill})
     * @param percentage the fraction (0.0-1.0) of positions to re-roll
     * @param players    players whose current standing position must not be touched
     */
    public static void reshuffle(Instance instance, Area area, double percentage, Collection<Player> players) {
        List<Vec> positions = area.positions();
        if (positions.isEmpty()) return;

        Set<Vec> excluded = new HashSet<>();
        for (Player player : players) {
            Pos playerPosition = player.getPosition();
            excluded.add(new Vec(Math.floor(playerPosition.x()), Math.floor(playerPosition.y() - 1), Math.floor(playerPosition.z())));
        }

        List<Vec> candidates = new ArrayList<>();
        for (Vec position : positions) {
            if (excluded.contains(position)) continue;
            if (instance.getBlock(position).compare(Block.REDSTONE_BLOCK)) continue; // static death block, keep it
            candidates.add(position);
        }
        if (candidates.isEmpty()) return;

        Collections.shuffle(candidates, ThreadLocalRandom.current());
        int amount = Math.min(candidates.size(), (int) Math.round(positions.size() * percentage));

        List<PushEntry> entries = area.data().push();
        for (int i = 0; i < amount; i++) {
            Vec position = candidates.get(i);
            instance.setBlock(position, pickWeightedBlock(entries, area.groundBlock(), false));
        }
    }

    private static boolean isWithinRadius(Point position, Point center, double radius) {
        double dx = position.x() - center.x();
        double dz = position.z() - center.z();
        return dx * dx + dz * dz <= radius * radius;
    }

    private static Block pickWeightedBlock(List<PushEntry> entries, Block fallback, boolean includeDeathBlock) {
        double roll = ThreadLocalRandom.current().nextDouble(); // 0.0 to 1.0
        if (includeDeathBlock && roll < DEATH_BLOCK_WEIGHT) {
            return Block.REDSTONE_BLOCK;
        }
        double cumulative = includeDeathBlock ? DEATH_BLOCK_WEIGHT : 0.0;
        for (PushEntry entry : entries) {
            if (entry.isGround()) continue;
            double p = Math.clamp(entry.getWeight(), 0.0, 1.0);
            cumulative += p;
            if (roll < cumulative) {
                return entry.getBlock();
            }
        }
        return fallback;
    }
}
