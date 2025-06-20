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
import net.theevilreaper.bounce.event.ScoreDeathUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class PlayerJumpTask {

    private static final Map<Block, Function<GameMap, Double>> blockPushMap = Map.of(
            Block.LIGHT_BLUE_STAINED_GLASS, GameMap::getDefaultPush,
            Block.LAPIS_BLOCK, GameMap::getIronPush,
            Block.GOLD_BLOCK, GameMap::getGoldPush,
            Block.EMERALD_BLOCK, GameMap::getEmeraldPush
    );

    private final Player player;
    private GameMap map;
    private Task task;
    private Block lastBlockBelow = Block.AIR; // Track previous block below player
    private long lastPushTime = 0;
    private static final long PUSH_COOLDOWN_MS = 200; // 200 ms cooldown

    public PlayerJumpTask(@NotNull Player player) {
        this.player = player;
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
        Pos playerPos = player.getPosition();
        Pos blockPos = playerPos.sub(0, 1, 0);
        Block blockBelow = instance.getBlock(blockPos);

        //System.out.printf("Player %s is on block: %s at position %s%n", player.getUsername(), blockBelow, blockPos);

        if (blockBelow.isAir()) {
            lastBlockBelow = Block.AIR; // reset when in air
            return;
        }

        if (blockBelow == Block.REDSTONE_BLOCK) {
            player.teleport(map.getGameSpawn());
            EventDispatcher.call(new ScoreDeathUpdateEvent(player));
            lastBlockBelow = blockBelow;
            return;
        }

        if (!blockBelow.compare(lastBlockBelow) && blockPushMap.containsKey(blockBelow)) {
            // Only push if player is falling
            if (player.getVelocity().y() < 0) {
                long now = System.currentTimeMillis();
                if (now - lastPushTime >= PUSH_COOLDOWN_MS) {
                    Function<GameMap, Double> pushFunc = blockPushMap.get(blockBelow);
                    double pushStrength = pushFunc.apply(map) * 10;

                    Vec push = new Vec(0, pushStrength, 0); // Only push upwards
                    player.setVelocity(push);

                    lastPushTime = now;
                }
            }
        }

        lastBlockBelow = blockBelow; // Important!
    }

    public void cancel() {
        if (this.task == null) return;
        this.task.cancel();
        this.task = null;
    }
}
