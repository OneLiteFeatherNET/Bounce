package net.theevilreaper.bounce.map;

import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.AreaFiller;
import net.theevilreaper.bounce.powerup.PowerUpSpawner;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Custom {@link InstanceContainer} which reshuffles its configured {@link Area} and drives an optional
 * {@link PowerUpSpawner} on its own tick instead of relying on separately scheduled tasks.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.0.0
 */
public final class BounceInstance extends InstanceContainer {

    private final @Nullable Area area;
    private final int shuffleIntervalTicks;
    private final double reshufflePercentage;
    private @Nullable PowerUpSpawner powerUpSpawner;

    public BounceInstance(UUID uuid, RegistryKey<DimensionType> dimensionType, @Nullable Area area, int shuffleIntervalTicks, double reshufflePercentage) {
        super(uuid, dimensionType);
        this.area = area;
        this.shuffleIntervalTicks = shuffleIntervalTicks;
        this.reshufflePercentage = reshufflePercentage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick(long time) {
        super.tick(time);
        if (powerUpSpawner != null) {
            powerUpSpawner.tick(this, getWorldAge());
        }
        if (area == null || shuffleIntervalTicks <= 0) return;
        if (getWorldAge() % shuffleIntervalTicks == 0) {
            AreaFiller.reshuffle(this, area, reshufflePercentage, getPlayers());
        }
    }

    /**
     * Starts spawning power-ups with the given spawner. A previously running spawner gets stopped first.
     *
     * @param spawner the spawner to use
     */
    public void startPowerUps(PowerUpSpawner spawner) {
        stopPowerUps();
        this.powerUpSpawner = spawner;
    }

    /**
     * Stops spawning power-ups and removes every power-up which is still alive.
     */
    public void stopPowerUps() {
        if (powerUpSpawner == null) return;
        powerUpSpawner.clear();
        powerUpSpawner = null;
    }
}
