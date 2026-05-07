package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class GroundArea implements Area {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroundArea.class);

    private final Vec min;
    private final Vec max;
    private final PushData data;
    private final Block groundBlock;
    private final List<Vec> positions;

    public GroundArea(Vec min, Vec max, Block groundBlock, PushData pushData) {
        this.min = min;
        this.max = max;
        this.data = pushData;
        this.groundBlock = groundBlock;
        this.positions = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculatePositions() {
        // Avoid double calculations
        if (!this.positions.isEmpty()) return;

        int minX = (int) Math.floor(Math.min(min.x(), max.x()));
        int maxX = (int) Math.floor(Math.max(min.x(), max.x()));
        int minZ = (int) Math.floor(Math.min(min.z(), max.z()));
        int maxZ = (int) Math.floor(Math.max(min.z(), max.z()));

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                positions.add(new Vec(x, min.y(), z));
            }
        }

        LOGGER.info("Calculated positions for area: {} to {} with {} positions", min, max, positions.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPositions() {
        return !this.positions.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData data() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec max() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec min() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    public Block groundBlock() {
        return groundBlock;
    }
}
