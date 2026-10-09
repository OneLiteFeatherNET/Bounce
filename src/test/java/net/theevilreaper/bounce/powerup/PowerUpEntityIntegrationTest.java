package net.theevilreaper.bounce.powerup;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.event.PlayerPowerUpPickupEvent;
import net.theevilreaper.bounce.player.BouncePlayer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class PowerUpEntityIntegrationTest {

    private static final Pos POWER_UP_POSITION = new Pos(0.5, 42.5, 0.5);

    @Test
    void testBobsUpAndDownViaInterpolatedTranslation(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();
        PowerUpEntity entity = new PowerUpEntity(PowerUpType.SPEED);
        entity.setInstance(instance, POWER_UP_POSITION).join();

        env.tick();
        ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();
        assertEquals(PowerUpType.SPEED.getIcon(), meta.getItemStack());
        assertEquals(PowerUpEntity.HALF_PERIOD_TICKS, meta.getTransformationInterpolationDuration());
        assertTrue(meta.getTranslation().y() > 0, "The first half period must move the power-up upwards");

        for (int i = 0; i < PowerUpEntity.HALF_PERIOD_TICKS; i++) {
            env.tick();
        }
        assertTrue(meta.getTranslation().y() < 0, "The second half period must move the power-up downwards");
        assertEquals(POWER_UP_POSITION, entity.getPosition(), "Bobbing must not move the server-side position");

        env.destroyInstance(instance, true);
    }

    @Test
    void testRoundActivePlayerPicksUpPowerUp(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();

        BouncePlayer player = (BouncePlayer) env.createPlayer(instance, new Pos(0.5, 41, 0.5));
        player.startRound();

        List<PlayerPowerUpPickupEvent> events = new ArrayList<>();
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPowerUpPickupEvent.class, events::add);

        PowerUpEntity entity = new PowerUpEntity(PowerUpType.SHIELD);
        entity.setInstance(instance, POWER_UP_POSITION).join();
        env.tick();

        assertEquals(1, events.size());
        assertSame(player, events.getFirst().getPlayer());
        assertEquals(PowerUpType.SHIELD, events.getFirst().getType());
        assertTrue(entity.isRemoved(), "A picked up power-up must remove itself");

        env.destroyInstance(instance, true);
    }

    @Test
    void testPlayersOutsideTheRoundOrOutOfReachDoNotPickUp(@NotNull Env env) {
        MinecraftServer.getConnectionManager().setPlayerProvider(BouncePlayer::new);
        Instance instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();

        env.createPlayer(instance, new Pos(0.5, 41, 0.5)); // inside the box, but not part of the round
        BouncePlayer farAway = (BouncePlayer) env.createPlayer(instance, new Pos(5.5, 41, 5.5));
        farAway.startRound();

        PowerUpEntity entity = new PowerUpEntity(PowerUpType.SHIELD);
        entity.setInstance(instance, POWER_UP_POSITION).join();
        env.tick();

        assertFalse(entity.isRemoved());

        env.destroyInstance(instance, true);
    }
}
