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
        if (!positions.isEmpty()) {
            return;
        }

        int minX = (int) Math.floor(Math.min(min.x(), max.x()));
        int maxX = (int) Math.floor(Math.max(min.x(), max.x()));
        int minZ = (int) Math.floor(Math.min(min.z(), max.z()));
        int maxZ = (int) Math.floor(Math.max(min.z(), max.z()));
        int targetY = (int) Math.floor(min.y());

        loadChunks(instance, minX, maxX, minZ, maxZ);

        scanPositions(instance, minX, maxX, minZ, maxZ, targetY);

        if (positions.isEmpty()) {
            scanPositions(instance, minX, maxX, minZ, maxZ, targetY - 1);
        }

        LOGGER.info(
                "Calculated positions for area: {} to {} with {} positions",
                min,
                max,
                positions.size()
        );
    }

    private void loadChunks(Instance instance, int minX, int maxX, int minZ, int maxZ) {
        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                futures.add(instance.loadChunk(chunkX, chunkZ));
            }
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    private void scanPositions(
            Instance instance,
            int minX,
            int maxX,
            int minZ,
            int maxZ,
            int y
    ) {
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (isAreaBlock(instance.getBlock(x, y, z))) {
                    positions.add(new Vec(x, y, z));
                }
            }
        }
    }

    /**
     * Checks if the given block is an area block.
     *
     * @param block to check
     * @return true yes otherwise false
     */
    private boolean isAreaBlock(Block block) {
        return block.compare(groundBlock) || block.compare(Block.REDSTONE_BLOCK) || data.hasBlock(block);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPositions() {
        return !positions.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vec> positions() {
        return Collections.unmodifiableList(positions);
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