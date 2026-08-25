package net.theevilreaper.bounce.map;

import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.AreaFiller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Custom {@link InstanceContainer} which reshuffles its configured {@link Area} on its own tick instead of relying
 * on a separately scheduled task.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class BounceInstance extends InstanceContainer {

    private final @Nullable Area area;
    private final int shuffleIntervalTicks;
    private final double reshufflePercentage;

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
        if (area == null || shuffleIntervalTicks <= 0) return;
        if (getWorldAge() % shuffleIntervalTicks == 0) {
            AreaFiller.reshuffle(this, area, reshufflePercentage, getPlayers());
        }
    }
}
