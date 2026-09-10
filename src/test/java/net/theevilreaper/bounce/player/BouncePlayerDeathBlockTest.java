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
import net.theevilreaper.bounce.listener.game.PlayerDeathBlockListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerDeathBlockListener(() -> gameSpawn));

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

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0);
        victim.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDeathBlockEvent.class, new PlayerDeathBlockListener(() -> gameSpawn));

        env.tick();
        env.tick();

        assertNotEquals(deathBlockPosition, victim.getPosition(), "Victim should have been teleported away from the death block");
        assertEquals(0, victim.getPoints(), "Without an attacker, no points must change hands");
    }
}
