package net.theevilreaper.bounce.setup.preview;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.common.push.PushPhysics;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PushPreviewEntityIntegrationTest {

    @Test
    void testPreviewBouncesThreeTimes(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance, new Pos(0, 42, 0));
        int pushValue = 3;
        Pos spawn = new Pos(0.5, 42, 3.5);

        PushPreviewEntity preview = new PushPreviewEntity(player, Block.SLIME_BLOCK, pushValue);
        preview.launch(instance, spawn);

        double highestY = spawn.y();
        int landings = 0;
        boolean rising = true;
        for (int i = 0; i < 500 && !preview.isRemoved(); i++) {
            double previousY = preview.getPosition().y();
            env.tick();
            double currentY = preview.getPosition().y();
            highestY = Math.max(highestY, currentY);
            if (rising && currentY < previousY) rising = false;
            if (!rising && currentY == spawn.y()) {
                landings++;
                rising = true;
            }
        }

        assertTrue(preview.isRemoved(), "The preview should remove itself after the last bounce");
        assertEquals(PushPreviewEntity.BOUNCES, landings, "The preview should land once per bounce");
        assertEquals(PushPhysics.peakHeight(pushValue), highestY - spawn.y(), 0.01);

        env.destroyInstance(instance, true);
    }

    @Test
    void testLaunchAllSpawnsOnePreviewPerEntry(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance, new Pos(0, 42, 0));
        List<PushEntry> entries = List.of(
                PushEntry.groundEntry(Block.WHITE_WOOL, 1),
                PushEntry.pushEntry(Block.SLIME_BLOCK, 3),
                PushEntry.pushEntry(Block.EMERALD_BLOCK, 5)
        );

        PushPreviewEntity.launchAll(player, entries);
        env.tick();

        List<Entity> previews = instance.getEntities().stream()
                .filter(PushPreviewEntity.class::isInstance)
                .toList();
        assertEquals(entries.size(), previews.size(), "Each entry should get its own preview");
        assertEquals(entries.size(), previews.stream().map(entity -> entity.getPosition().blockX()).distinct().count(),
                "The previews should be placed next to each other");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPreviewIsOnlyVisibleToViewer(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player viewer = env.createPlayer(instance, new Pos(0, 42, 0));
        Player other = env.createPlayer(instance, new Pos(1, 42, 0));

        PushPreviewEntity preview = new PushPreviewEntity(viewer, Block.SLIME_BLOCK, 5);
        preview.launch(instance, new Pos(0.5, 42, 3.5));
        env.tick();

        assertTrue(preview.getViewers().contains(viewer), "The requesting player should see the preview");
        assertFalse(preview.getViewers().contains(other), "Other players should not see the preview");

        env.destroyInstance(instance, true);
    }

    @Test
    void testIsRunningUntilPreviewIsRemoved(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player viewer = env.createPlayer(instance, new Pos(0, 42, 0));
        Player other = env.createPlayer(instance, new Pos(1, 42, 0));

        assertFalse(PushPreviewEntity.isRunning(viewer), "No preview should run before one is launched");

        PushPreviewEntity preview = new PushPreviewEntity(viewer, Block.SLIME_BLOCK, 1);
        preview.launch(instance, new Pos(0.5, 42, 3.5));
        env.tick();

        assertTrue(PushPreviewEntity.isRunning(viewer), "The preview of the viewer should be running");
        assertFalse(PushPreviewEntity.isRunning(other), "A preview of another player should not block this player");

        for (int i = 0; i < 200 && !preview.isRemoved(); i++) {
            env.tick();
        }

        assertFalse(PushPreviewEntity.isRunning(viewer), "The preview should not run after it was removed");

        env.destroyInstance(instance, true);
    }
}
