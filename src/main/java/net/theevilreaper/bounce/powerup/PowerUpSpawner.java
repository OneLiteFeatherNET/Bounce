package net.theevilreaper.bounce.powerup;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import net.theevilreaper.bounce.common.ground.Area;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * Spawns {@link PowerUpEntity} instances at random positions of an {@link Area}.
 * <p>
 * The spawn height is relative to the ground: a power-up floats {@code height} blocks above the top surface of a
 * randomly picked ground block. The spawner is driven by {@link #tick(Instance, long)} and keeps at most
 * {@code maxActive} power-ups alive at the same time.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class PowerUpSpawner {

    public static final int DEFAULT_INTERVAL_TICKS = 200;
    public static final int DEFAULT_MAX_ACTIVE = 3;

    private static final int MAX_SPAWN_ATTEMPTS = 5;
    private static final double MIN_DISTANCE_SQUARED = 4.0;

    private final Area area;
    private final double height;
    private final int intervalTicks;
    private final int maxActive;
    private final RandomGenerator random;
    private final List<PowerUpEntity> active;

    /**
     * Creates a new spawner with the default interval and limit.
     *
     * @param area   the area whose ground positions are used
     * @param height the height above the ground at which power-ups spawn
     */
    public PowerUpSpawner(Area area, double height) {
        this(area, height, DEFAULT_INTERVAL_TICKS, DEFAULT_MAX_ACTIVE, new Random());
    }

    /**
     * Creates a new spawner.
     *
     * @param area          the area whose ground positions are used
     * @param height        the height above the ground at which power-ups spawn
     * @param intervalTicks the amount of ticks between two spawn attempts
     * @param maxActive     the maximum amount of power-ups alive at the same time
     * @param random        the random source used to pick positions and types
     */
    public PowerUpSpawner(Area area, double height, int intervalTicks, int maxActive, RandomGenerator random) {
        this.area = area;
        this.height = height;
        this.intervalTicks = intervalTicks;
        this.maxActive = maxActive;
        this.random = random;
        this.active = new ArrayList<>();
    }

    /**
     * Spawns a new power-up once the interval elapsed and the limit is not reached.
     *
     * @param instance the instance to spawn in
     * @param worldAge the current age of the instance
     */
    public void tick(Instance instance, long worldAge) {
        this.active.removeIf(Entity::isRemoved);
        if (this.intervalTicks <= 0 || worldAge % this.intervalTicks != 0) return;
        if (this.active.size() >= this.maxActive) return;

        List<Vec> positions = this.area.positions();
        if (positions.isEmpty()) return;

        PowerUpType[] types = PowerUpType.values();
        for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
            Pos spawn = toSpawnPosition(positions.get(this.random.nextInt(positions.size())));
            if (isOccupied(spawn)) continue;

            PowerUpEntity entity = new PowerUpEntity(types[this.random.nextInt(types.length)]);
            entity.setInstance(instance, spawn);
            this.active.add(entity);
            return;
        }
    }

    /**
     * Removes all power-ups which were spawned by this spawner.
     */
    public void clear() {
        this.active.forEach(Entity::remove);
        this.active.clear();
    }

    /**
     * Returns the power-ups which were spawned by this spawner and may still be alive.
     *
     * @return an unmodifiable view of the power-ups
     */
    public @UnmodifiableView List<PowerUpEntity> getActive() {
        return Collections.unmodifiableList(this.active);
    }

    private Pos toSpawnPosition(Vec ground) {
        // +1 to start on the top surface of the ground block instead of its bottom
        return new Pos(ground.blockX() + 0.5, ground.blockY() + 1 + this.height, ground.blockZ() + 0.5);
    }

    private boolean isOccupied(Pos spawn) {
        for (PowerUpEntity entity : this.active) {
            if (entity.getPosition().distanceSquared(spawn) < MIN_DISTANCE_SQUARED) return true;
        }
        return false;
    }
}
