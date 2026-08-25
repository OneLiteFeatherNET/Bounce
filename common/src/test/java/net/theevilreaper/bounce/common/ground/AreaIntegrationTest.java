package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class AreaIntegrationTest {

    @Test
    void testCalculatePositionsOnlyIncludesMatchingGroundBlock(@NotNull Env env) {
        Instance instance = env.createFlatInstance();

        instance.setBlock(0, 0, 0, Block.AMETHYST_BLOCK);
        instance.setBlock(1, 0, 0, Block.AMETHYST_BLOCK);
        instance.setBlock(2, 0, 0, Block.STONE); // not the configured ground block

        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(2, 0, 0), Block.AMETHYST_BLOCK, new PushData(List.of()));
        assertFalse(area.hasPositions());

        area.calculatePositions(instance);

        assertTrue(area.hasPositions());
        assertEquals(2, area.positions().size());
        assertTrue(area.positions().contains(new Vec(0, 0, 0)));
        assertTrue(area.positions().contains(new Vec(1, 0, 0)));
        assertFalse(area.positions().contains(new Vec(2, 0, 0)));

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty());
    }

    @Test
    void testCalculatePositionsIsIdempotent(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        instance.setBlock(0, 0, 0, Block.GLASS);

        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(0, 0, 0), Block.GLASS, new PushData(List.of()));
        area.calculatePositions(instance);
        instance.setBlock(0, 0, 0, Block.STONE); // world changes after the first scan

        area.calculatePositions(instance); // second call must be a no-op

        assertEquals(1, area.positions().size(), "A second call must not re-scan or clear the already computed positions");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPositionsIsUnmodifiable(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        instance.setBlock(0, 0, 0, Block.GLASS);

        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(0, 0, 0), Block.GLASS, new PushData(List.of()));
        area.calculatePositions(instance);

        Vec vec = new Vec(9, 9, 9);
        assertThrows(UnsupportedOperationException.class, () -> area.positions().add(vec));

        env.destroyInstance(instance, true);
    }

    @Test
    void testCalculatePositionsLoadsChunksInArea(@NotNull Env env) {
        Instance instance = env.createFlatInstance();

        // Coordinates spanning multiple chunks (e.g. chunk -2, 3 to chunk -1, 4)
        Vec min = new Vec(-25, 60, -25);
        Vec max = new Vec(25, 60, 25);

        Area area = new GroundArea(min, max, Block.STONE, new PushData(List.of()));
        assertDoesNotThrow(() -> area.calculatePositions(instance));

        env.destroyInstance(instance, true);
    }

    @Test
    void testCalculatePositionsIncludesConfiguredPushBlocks(@NotNull Env env) {
        Instance instance = env.createFlatInstance();

        instance.setBlock(0, 0, 0, Block.AMETHYST_BLOCK); // ground block
        instance.setBlock(1, 0, 0, Block.GOLD_BLOCK);      // existing push block
        instance.setBlock(2, 0, 0, Block.STONE);           // unrelated block

        PushData pushData = PushData.builder()
                .add(net.theevilreaper.bounce.common.push.PushEntry.groundEntry(Block.AMETHYST_BLOCK, 1))
                .add(net.theevilreaper.bounce.common.push.PushEntry.pushEntry(Block.GOLD_BLOCK, 2))
                .build();

        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(2, 0, 0), Block.AMETHYST_BLOCK, pushData);
        area.calculatePositions(instance);

        assertEquals(2, area.positions().size());
        assertTrue(area.positions().contains(new Vec(0, 0, 0)));
        assertTrue(area.positions().contains(new Vec(1, 0, 0)));
        assertFalse(area.positions().contains(new Vec(2, 0, 0)));

        env.destroyInstance(instance, true);
    }

    @Test
    void testCalculatePositionsScansLayerBelowWhenStandingOnPlatform(@NotNull Env env) {
        Instance instance = env.createFlatInstance();

        // Platform block is placed at Y=63, air at Y=64
        instance.setBlock(0, 63, 0, Block.GLASS);
        instance.setBlock(0, 64, 0, Block.AIR);

        // Area captured at player feet level (Y=64)
        Area area = new GroundArea(new Vec(0, 64, 0), new Vec(0, 64, 0), Block.GLASS, new PushData(List.of()));
        area.calculatePositions(instance);

        assertEquals(1, area.positions().size(), "Should fallback to Y=63 when Y=64 has no ground blocks");
        assertEquals(new Vec(0, 63, 0), area.positions().getFirst());

        env.destroyInstance(instance, true);
    }
}
