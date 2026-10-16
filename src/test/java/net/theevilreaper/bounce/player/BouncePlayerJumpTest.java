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
import net.theevilreaper.bounce.powerup.PowerUpType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        GameMap map = new GameMap("test", padPosition, padPosition, pushData, List.of(), null, 0, 0, 0, 0);
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

    @Test
    void testPadUnderTheHitboxCornerPushesThePlayer(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        // Only the diagonal block touches the hitbox corner of a player standing at x/z 0.8
        instance.setBlock(1, 40, 1, Block.DIAMOND_BLOCK);

        Pos cornerPosition = new Pos(0.8, 41, 0.8);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, cornerPosition);
        player.startRound();

        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        GameMap map = new GameMap("test", cornerPosition, cornerPosition, pushData, List.of(), null, 0, 0, 0, 0);
        player.startJumping(map, pushData);

        player.setVelocity(FALLING);
        env.tick();

        assertTrue(player.getVelocity().y() > 0, "A pad under the hitbox corner must push the player");
    }

    @Test
    void testStandingOnTwoPadsUsesTheStrongestPush(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        // The weak pad is under most of the hitbox, the strong one only under its edge
        instance.setBlock(0, 40, 0, Block.DIAMOND_BLOCK);
        instance.setBlock(1, 40, 0, Block.EMERALD_BLOCK);

        Pos betweenPads = new Pos(0.8, 41, 0.5);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, betweenPads);
        player.startRound();

        PushData pushData = new PushData(List.of(
                PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0),
                PushEntry.pushEntry(Block.EMERALD_BLOCK, 3, 1.0)
        ));
        GameMap map = new GameMap("test", betweenPads, betweenPads, pushData, List.of(), null, 0, 0, 0, 0);
        player.startJumping(map, pushData);

        player.setVelocity(FALLING);
        env.tick();

        assertEquals(30, player.getVelocity().y(), "The strongest pad below the player must be used");
    }

    @Test
    void testSuperJumpUsesTheMultiplierOfTheMap(@NotNull Env env) {
        assertEquals(20, superJumpVelocity(env, 2.0), 1e-9, "The super jump must scale the push by the multiplier of the map");
    }

    @Test
    void testSuperJumpFallsBackToTheDefaultMultiplier(@NotNull Env env) {
        assertEquals(10 * GameMap.DEFAULT_SUPER_JUMP_MULTIPLIER, superJumpVelocity(env, 0), 1e-9,
                "Maps without a multiplier must use the default one");
    }

    private double superJumpVelocity(Env env, double superJumpMultiplier) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.DIAMOND_BLOCK);

        Pos padPosition = new Pos(0.5, 41, 0.5);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, padPosition);
        player.startRound();
        player.activatePowerUp(PowerUpType.SUPER_JUMP);

        PushData pushData = new PushData(List.of(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 1.0)));
        GameMap map = new GameMap("test", padPosition, padPosition, pushData, List.of(), null, 0, 0, 0, superJumpMultiplier);
        player.startJumping(map, pushData);
        player.setVelocity(FALLING);
        env.tick();

        double velocity = player.getVelocity().y();
        env.destroyInstance(instance, true);
        return velocity;
    }
}
