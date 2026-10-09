package net.theevilreaper.bounce.powerup;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PowerUpSpawnerIntegrationTest {

    private static Area createArea(Instance instance) {
        instance.loadChunk(0, 0).join();
        instance.setBlock(0, 40, 0, Block.GLASS);
        Area area = new GroundArea(new Vec(0, 40, 0), new Vec(0, 40, 0), Block.GLASS, new PushData(List.of()));
        area.calculatePositions(instance);
        return area;
    }

    @Test
    void testSpawnsRelativeToTheGroundOnceTheIntervalElapses(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        PowerUpSpawner spawner = new PowerUpSpawner(createArea(instance), 1.5, 5, 3, new Random(1));

        spawner.tick(instance, 4);
        assertTrue(spawner.getActive().isEmpty(), "No power-up must spawn before the interval elapsed");

        spawner.tick(instance, 5);
        assertEquals(1, spawner.getActive().size());

        PowerUpEntity entity = spawner.getActive().getFirst();
        assertEquals(instance, entity.getInstance());
        assertEquals(new Pos(0.5, 42.5, 0.5), entity.getPosition(), "The power-up must float 1.5 blocks above the top of the ground block");

        env.destroyInstance(instance, true);
    }

    @Test
    void testDoesNotStackPowerUpsOnTheSamePosition(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        PowerUpSpawner spawner = new PowerUpSpawner(createArea(instance), 1.5, 5, 3, new Random(1));

        spawner.tick(instance, 5);
        spawner.tick(instance, 10);

        assertEquals(1, spawner.getActive().size(), "A single ground position must never hold more than one power-up");

        env.destroyInstance(instance, true);
    }

    @Test
    void testRespawnsAfterPickupAndClearRemovesEverything(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        PowerUpSpawner spawner = new PowerUpSpawner(createArea(instance), 1.5, 5, 1, new Random(1));

        spawner.tick(instance, 5);
        PowerUpEntity first = spawner.getActive().getFirst();
        first.remove();

        spawner.tick(instance, 10);
        assertEquals(1, spawner.getActive().size());
        PowerUpEntity second = spawner.getActive().getFirst();
        assertNotSame(first, second, "A removed power-up must free its slot for a new one");

        spawner.clear();
        assertTrue(spawner.getActive().isEmpty());
        assertTrue(second.isRemoved(), "Clearing the spawner must remove the alive power-ups");

        env.destroyInstance(instance, true);
    }
}
