package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerJumpTest {

    private static final Vec FALLING = new Vec(0, -1, 0);

    @Test
    void testLandingDuringTheCooldownDoesNotBlockTheNextPush(@NotNull Env env) throws InterruptedException {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.DIAMOND_BLOCK);

        Pos padPosition = new Pos(0.5, 41, 0.5);
        Pos stonePosition = new Pos(5.5, 41, 5.5);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, padPosition);
        player.startRound();

        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        GameMap map = new GameMap("test", padPosition, padPosition, pushData, List.of(), null, 0, 0, 0);
        player.startJumping(map, pushData);

        // First push starts the cooldown
        env.tick();
        // Leave the pad and land on it again while the cooldown is still running
        player.teleport(stonePosition).join();
        env.tick();
        player.teleport(padPosition).join();
        player.setVelocity(FALLING);
        env.tick();

        Thread.sleep(BouncePlayer.PUSH_COOLDOWN_MS + 50);
        player.setVelocity(FALLING);
        env.tick();

        assertTrue(player.getVelocity().y() > 0, "A player who landed during the cooldown must be pushed once it expired");
    }
}
