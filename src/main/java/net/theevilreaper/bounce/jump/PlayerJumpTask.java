package net.theevilreaper.bounce.jump;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.theevilreaper.bounce.common.map.GameMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class PlayerJumpTask {

    private static final Map<Block, Function<GameMap, Double>> blockPushMap = Map.of(
            Block.GLASS, GameMap::getDefaultPush,
            Block.LAPIS_BLOCK, GameMap::getIronPush,
            Block.GOLD_BLOCK, GameMap::getGoldPush,
            Block.EMERALD_BLOCK, GameMap::getEmeraldPush
    );

    private final Player player;
    private GameMap map;
    private Task task;

    public PlayerJumpTask(@NotNull Player player) {
        this.player = player;
    }

    public void start(@NotNull GameMap bounceMap) {
        if (this.task != null) return;
        this.map = bounceMap;
        this.task = MinecraftServer.getSchedulerManager().buildTask(this::onTick)
                .repeat(TaskSchedule.millis(150))
                .schedule();
    }

    private void onTick() {
        Instance instance = player.getInstance();
        Pos playerPos = player.getPosition();
        Block block = instance.getBlock(playerPos.sub(0, -1, 0));

        if (block.isAir()) return;

        if (block == Block.REDSTONE_BLOCK) {
            player.teleport(map.getGameSpawn());
            //TODO: Reset data
            return;
        }

        Function<GameMap, Double> pushFunc = blockPushMap.get(block);
        if (pushFunc != null) {
            double push = pushFunc.apply(map);
            Vec velocity = player.getVelocity();
            player.setVelocity(new Vec(velocity.x(), push, velocity.z()));
        }
    }

    public void cancel() {
        if (this.task == null) return;
        this.task.cancel();
        this.task = null;
    }
}
