package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public void calculatePositions(Instance instance) {
        // Avoid double calculations
        if (!this.positions.isEmpty()) return;

        int minX = (int) Math.floor(Math.min(min.x(), max.x()));
        int maxX = (int) Math.floor(Math.max(min.x(), max.x()));
        int minZ = (int) Math.floor(Math.min(min.z(), max.z()));
        int maxZ = (int) Math.floor(Math.max(min.z(), max.z()));
        int targetY = (int) Math.floor(min.y());

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        List<CompletableFuture<Chunk>> chunkFutures = new ArrayList<>();
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                chunkFutures.add(instance.loadChunk(cx, cz));
            }
        }
        CompletableFuture.allOf(chunkFutures.toArray(new CompletableFuture[0])).join();

        // Scan the single 2D plane at targetY
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (isAreaBlock(instance.getBlock(x, targetY, z))) {
                    positions.add(new Vec(x, targetY, z));
                }
            }
        }

        // If no positions were found at targetY, try scanning targetY - 1
        // in case coordinates were captured while standing on top of the ground platform
        if (positions.isEmpty()) {
            int scanY = targetY - 1;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (isAreaBlock(instance.getBlock(x, scanY, z))) {
                        positions.add(new Vec(x, scanY, z));
                    }
                }
            }
        }

        LOGGER.info("Calculated positions for area: {} to {} with {} positions", min, max, positions.size());
    }

    private boolean isAreaBlock(Block block) {
        if (block.compare(groundBlock) || block.compare(Block.REDSTONE_BLOCK)) {
            return true;
        }
        if (data != null && data.push() != null) {
            for (var entry : data.push()) {
                if (block.compare(entry.getBlock())) {
                    return true;
                }
            }
        }
        return false;
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
    public List<Vec> positions() {
        return Collections.unmodifiableList(this.positions);
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
