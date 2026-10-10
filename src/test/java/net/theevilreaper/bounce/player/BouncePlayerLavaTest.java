package net.theevilreaper.bounce.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.listener.game.PlayerLavaListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MicrotusExtension.class)
class BouncePlayerLavaTest {

    @Test
    void testFallingIntoLavaTeleportsPlayerToSpawn(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, Block.LAVA);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos lavaPosition = new Pos(0.5, 41, 0.5);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, lavaPosition);
        player.startRound();

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0, 0);
        player.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLavaEvent.class, new PlayerLavaListener(() -> gameSpawn));

        env.tick();
        env.tick();

        assertNotEquals(lavaPosition, player.getPosition(), "Player should have been teleported away from the lava position");
    }

    @Test
    void testStandingInsideLavaTeleportsPlayerToSpawn(@NotNull Env env) {
        assertTeleportedWhenInside(env, Block.LAVA);
    }

    @Test
    void testStandingInsideFlowingLavaTeleportsPlayerToSpawn(@NotNull Env env) {
        assertTeleportedWhenInside(env, Block.LAVA.withProperty("level", "3"));
    }

    /**
     * A fast falling player skips the window above the lava and ends up inside it, with the feet on the
     * block below the lava. The check must still detect the lava in that case.
     */
    private static void assertTeleportedWhenInside(Env env, Block lava) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);

        Instance instance = env.createFlatInstance();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setBlock(0, 40, 0, lava);

        Pos gameSpawn = new Pos(10.5, 45, 10.5);
        Pos insideLavaPosition = new Pos(0.5, 40, 0.5);
        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, insideLavaPosition);
        player.startRound();

        GameMap map = new GameMap("test", gameSpawn, gameSpawn, new PushData(List.of()), List.of(), null, 0, 0);
        player.startJumping(map, map.getPushData());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLavaEvent.class, new PlayerLavaListener(() -> gameSpawn));

        env.tick();
        env.tick();

        assertNotEquals(insideLavaPosition, player.getPosition(), "Player inside the lava should have been teleported away");
    }
}
