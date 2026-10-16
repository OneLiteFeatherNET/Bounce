package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.PlayerDeathBlockEvent;
import net.theevilreaper.bounce.listener.game.PlayerHazardListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerDeathBlockTest {

    @Test
    void testLandingOnDeathBlockWithAttackerAwardsPointsToAttacker(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.REDSTONE_BLOCK);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos deathBlockPosition = new Pos(0.5, 41, 0.5);

        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, deathBlockPosition);
        victim.startRound();
        BouncePlayer attacker = (BouncePlayer) env.createPlayer(instance, gameSpawn);
        attacker.startRound();
        victim.setLastDamager(attacker);

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerHazardListener(() -> gameSpawn)::accept);

        env.tick();
        env.tick();

        assertNotEquals(deathBlockPosition, victim.getPosition(), "Victim should have been teleported away from the death block");
        assertEquals(10, attacker.getPoints(), "The attacker must be awarded points for the kill");
    }

    @Test
    void testLandingOnDeathBlockWithoutAttackerAwardsNoPoints(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.REDSTONE_BLOCK);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos deathBlockPosition = new Pos(0.5, 41, 0.5);

        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, deathBlockPosition);
        victim.startRound();

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerHazardListener(() -> gameSpawn)::accept);

        env.tick();
        env.tick();

        assertNotEquals(deathBlockPosition, victim.getPosition(), "Victim should have been teleported away from the death block");
        assertEquals(0, victim.getPoints(), "Without an attacker, no points must change hands");
    }

    @Test
    void testLandingOnDeathBlockAfterAttackerLeftAwardsNoPoints(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.REDSTONE_BLOCK);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos deathBlockPosition = new Pos(0.5, 41, 0.5);

        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, deathBlockPosition);
        victim.startRound();
        BouncePlayer attacker = (BouncePlayer) env.createPlayer(instance, gameSpawn);
        attacker.startRound();
        victim.setLastDamager(attacker);
        // Leaving the game ends the round for the attacker
        attacker.endRound();

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerHazardListener(() -> gameSpawn)::accept);

        env.tick();
        env.tick();

        assertNotEquals(deathBlockPosition, victim.getPosition(), "Victim should have been teleported away from the death block");
        assertEquals(0, attacker.getPoints(), "An attacker who left the round must not be awarded points");
        assertNull(victim.getLastDamager(), "The last damager must be reset after the death");
    }

    @Test
    void testDeathBlockUnderTheHitboxCornerTeleportsPlayer(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        // Only the diagonal block touches the hitbox corner of a player standing at x/z 0.8
        instance.setBlock(1, 40, 1, Block.REDSTONE_BLOCK);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos cornerPosition = new Pos(0.8, 41, 0.8);

        BouncePlayer victim = (BouncePlayer) env.createPlayer(instance, cornerPosition);
        victim.startRound();

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerHazardListener(() -> gameSpawn)::accept);

        env.tick();
        env.tick();

        assertNotEquals(cornerPosition, victim.getPosition(), "A death block under the hitbox corner must teleport the player");
    }
}
