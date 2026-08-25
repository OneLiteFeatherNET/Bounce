package net.theevilreaper.bounce.map;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.DimensionType;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class BounceInstanceIntegrationTest {

    @Test
    void testTickReshufflesAreaOnceIntervalElapses(@NotNull Env env) {
        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(4, 0, 0), Block.GLASS, pushData);

        BounceInstance instance = new BounceInstance(UUID.randomUUID(), DimensionType.OVERWORLD, area, 5, 0.1);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        env.process().instance().registerInstance(instance);

        for (int x = 0; x <= 4; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }
        area.calculatePositions(instance);

        for (int i = 0; i < 4; i++) {
            env.tick();
        }
        for (int x = 0; x <= 4; x++) {
            assertTrue(instance.getBlock(x, 0, 0).compare(Block.GLASS), "Area must not reshuffle before the configured interval elapses");
        }

        env.tick(); // 5th tick reaches the configured interval

        long diamondCount = 0;
        for (int x = 0; x <= 4; x++) {
            if (instance.getBlock(x, 0, 0).compare(Block.DIAMOND_BLOCK)) diamondCount++;
        }
        assertEquals(1, diamondCount, "10% of the 5 positions must be reshuffled to diamond block once the configured interval elapses");

        env.destroyInstance(instance, true);
    }

    @Test
    void testTickHonorsAConfiguredReshufflePercentage(@NotNull Env env) {
        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(4, 0, 0), Block.GLASS, pushData);

        BounceInstance instance = new BounceInstance(UUID.randomUUID(), DimensionType.OVERWORLD, area, 5, 1.0);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        env.process().instance().registerInstance(instance);

        for (int x = 0; x <= 4; x++) {
            instance.setBlock(x, 0, 0, Block.GLASS);
        }
        area.calculatePositions(instance);

        for (int i = 0; i < 5; i++) {
            env.tick();
        }

        for (int x = 0; x <= 4; x++) {
            assertTrue(instance.getBlock(x, 0, 0).compare(Block.DIAMOND_BLOCK), "A 100% reshuffle percentage must reshuffle every position");
        }

        env.destroyInstance(instance, true);
    }

    @Test
    void testTickDoesNothingWhenNoAreaIsConfigured(@NotNull Env env) {
        BounceInstance instance = new BounceInstance(UUID.randomUUID(), DimensionType.OVERWORLD, null, 5, 0.1);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        env.process().instance().registerInstance(instance);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                env.tick();
            }
        });

        env.destroyInstance(instance, true);
    }

    @Test
    void testTickSkipsReshuffleWhenIntervalIsNotPositive(@NotNull Env env) {
        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        Area area = new GroundArea(new Vec(0, 0, 0), new Vec(0, 0, 0), Block.GLASS, pushData);

        BounceInstance instance = new BounceInstance(UUID.randomUUID(), DimensionType.OVERWORLD, area, 0, 0.1);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        env.process().instance().registerInstance(instance);

        instance.setBlock(0, 0, 0, Block.GLASS);
        area.calculatePositions(instance);

        for (int i = 0; i < 10; i++) {
            env.tick();
        }

        assertTrue(instance.getBlock(0, 0, 0).compare(Block.GLASS), "A non-positive interval must never trigger a reshuffle");

        env.destroyInstance(instance, true);
    }
}
