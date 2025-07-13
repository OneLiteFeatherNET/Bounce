package net.theevilreaper.bounce.jump;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.ScoreDeathUpdateEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.NonExtendable
public final class PlayerJumpTask {

    private static final long PUSH_COOLDOWN_MS = 200; // 200 ms cooldown

    private final Player player;
    private final PushData pushData;

    private GameMap map;
    private Task task;
    private Block lastBlockBelow = Block.AIR; // Track previous block below player
    private long lastPushTime = 0;

    public PlayerJumpTask(@NotNull Player player, @NotNull PushData pushData) {
        this.player = player;
        this.pushData = pushData;
    }

    public void start(@NotNull GameMap bounceMap) {
        if (this.task != null) return;
        this.map = bounceMap;
        this.task = MinecraftServer.getSchedulerManager().buildTask(this::onTick)
                .repeat(TaskSchedule.millis(100)) // faster check for smoother feeling
                .schedule();
    }

    private void onTick() {
        Instance instance = player.getInstance();
        List<int[]> checkPositions = getCheckPositions();

        Block foundJumpBlock = null;

        for (int[] pos : checkPositions) {
            Block block = instance.getBlock(pos[0], pos[1], pos[2]);

            if (block == Block.REDSTONE_BLOCK) {
                player.teleport(map.getGameSpawn());
                EventDispatcher.call(new ScoreDeathUpdateEvent(player));
                lastBlockBelow = block;
                return;
            }

            if (block == Block.LAVA) {
                player.teleport(map.getGameSpawn());
                lastBlockBelow = block;
                return;
            }

            if (pushData.hasBlock(block) && !block.compare(lastBlockBelow)) {
                foundJumpBlock = block;
            }
        }

        if (foundJumpBlock == null) {
            lastBlockBelow = Block.AIR;
            return;
        }
        if (player.getVelocity().y() < 0) {
            long now = System.currentTimeMillis();
            if (now - lastPushTime >= PUSH_COOLDOWN_MS) {
                double pushStrength = pushData.getPush(foundJumpBlock) * 10;
                Vec push = new Vec(0, pushStrength, 0); // Only push upwards
                player.setVelocity(push);
                lastPushTime = now;
            }
        }
        lastBlockBelow = foundJumpBlock;
    }

    private @NotNull List<int[]> getCheckPositions() {
        Pos playerPos = player.getPosition();
        double halfWidth = 0.3;
        double minX = playerPos.x() - halfWidth;
        double maxX = playerPos.x() + halfWidth;
        double minZ = playerPos.z() - halfWidth;
        double maxZ = playerPos.z() + halfWidth;
        int y = (int) Math.floor(playerPos.y() - 1);

        return List.of(
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(playerPos.z())}, // center
                new int[]{(int) Math.floor(minX), y, (int) Math.floor(playerPos.z())},         // minX
                new int[]{(int) Math.floor(maxX), y, (int) Math.floor(playerPos.z())},         // maxX
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(minZ)},         // minZ
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(maxZ)}          // maxZ
        );
    }

    public void cancel() {
        if (this.task == null) return;
        this.task.cancel();
        this.task = null;
    }
}
