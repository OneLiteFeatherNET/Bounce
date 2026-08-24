package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class AreaFillerIntegrationTest {

    @Test
    void testFillFallsBackToGroundBlockWhenPushDataEmpty(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        for (int x = 0; x < 5; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }

        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(4, 0, 0), Block.GLASS, new PushData(List.of()));

        AreaFiller.fill(instance, area);

        for (Vec position : area.positions()) {
            assertTrue(instance.getBlock(position).compare(Block.GLASS));
        }

        env.destroyInstance(instance, true);
    }

    @Test
    void testFillNeverPicksAZeroWeightEntry(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        for (int x = 0; x < 30; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }

        PushData pushData = new PushData(List.of(
                PushEntry.groundEntry(Block.GLASS, 1, 1.0),
                PushEntry.pushEntry(Block.DIAMOND_BLOCK, 5, 0.0)
        ));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(29, 0, 0), Block.GLASS, pushData);

        AreaFiller.fill(instance, area);

        for (Vec position : area.positions()) {
            assertFalse(instance.getBlock(position).compare(Block.DIAMOND_BLOCK), "A weight of 0 must never be picked");
        }

        env.destroyInstance(instance, true);
    }

    @Test
    void testFillDistributionRoughlyFollowsWeights(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        for (int x = 0; x < 100; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }

        PushData pushData = new PushData(List.of(
                PushEntry.groundEntry(Block.GLASS, 1, 1.0),
                PushEntry.pushEntry(Block.GOLD_BLOCK, 1, 0.8) // 80% gold, 20% glass
        ));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(99, 0, 0), Block.GLASS, pushData);

        AreaFiller.fill(instance, area);

        long goldCount = area.positions().stream().filter(pos -> instance.getBlock(pos).compare(Block.GOLD_BLOCK)).count();
        long glassCount = area.positions().stream().filter(pos -> instance.getBlock(pos).compare(Block.GLASS)).count();

        assertEquals(100, goldCount + glassCount);
        assertTrue(goldCount > glassCount, "Gold has 80% probability and should dominate the distribution");

        env.destroyInstance(instance, true);
    }

    @Test
    void testReshuffleSkipsThePositionUnderAPlayer(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        for (int x = 0; x < 10; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }
        // A block no fill/reshuffle would ever place, so if it's still here afterwards we know the position was skipped
        instance.setBlock(0, 0, 0, Block.WATER);

        Player player = env.createPlayer(instance);
        player.teleport(new net.minestom.server.coordinate.Pos(0.5, 1, 0.5)).join();

        PushData pushData = new PushData(List.of(PushEntry.groundEntry(Block.GLASS, 1, 1.0)));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(9, 0, 0), Block.GLASS, pushData);
        area.calculatePositions(instance);

        AreaFiller.reshuffle(instance, area, 1.0, List.of(player));

        assertTrue(instance.getBlock(0, 0, 0).compare(Block.WATER), "The position under the player must be left untouched");

        env.destroyInstance(instance, true);
    }

    @Test
    void testReshuffleExcludesPlayerPositionEvenWhenItIsAValidCandidate(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        for (int x = 0; x < 10; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }

        Player player = env.createPlayer(instance);
        player.teleport(new net.minestom.server.coordinate.Pos(0.5, 1, 0.5)).join();

        // Only a non-ground entry with probability 1.0 is configured, so every reshuffled position is guaranteed to
        // become DIAMOND_BLOCK unless it was excluded because a player stands on it.
        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(9, 0, 0), Block.GLASS, pushData);
        area.calculatePositions(instance);

        AreaFiller.reshuffle(instance, area, 1.0, List.of(player));

        assertTrue(instance.getBlock(0, 0, 0).compare(Block.GLASS), "The position under the player was a valid candidate but must remain untouched");
        for (int x = 1; x < 10; x++) {
            assertTrue(instance.getBlock(x, 0, 0).compare(Block.DIAMOND_BLOCK), "Every other position should have been reshuffled");
        }

        env.destroyInstance(instance, true);
    }
}
