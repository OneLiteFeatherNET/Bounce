package net.theevilreaper.bounce.profile;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class ProfileServiceIntegrationTest {

    @Test
    void testProfileServiceAddAndGet(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        ProfileService service = new ProfileService();
        assertTrue(service.getProfileMap().isEmpty());

        BounceProfile profile = service.add(player);
        assertNotNull(profile);
        assertEquals(player.getUuid(), profile.getPlayer().getUuid());

        assertEquals(profile, service.get(player));
        assertEquals(profile, service.get(player.getUuid()));
        assertEquals(1, service.getProfileMap().size());

        env.destroyInstance(instance, true);
    }

    @Test
    void testProfileServiceClearWithCallbackClearsMap(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player1 = env.createPlayer(instance);
        Player player2 = env.createPlayer(instance);

        ProfileService service = new ProfileService();
        service.add(player1);
        service.add(player2);

        assertEquals(2, service.getProfileMap().size());

        AtomicInteger processedCount = new AtomicInteger(0);
        service.clear(profile -> processedCount.incrementAndGet());

        assertEquals(2, processedCount.get(), "Callback should be executed for each profile");
        assertTrue(service.getProfileMap().isEmpty(), "Profile map should be cleared after clear(callback)");

        env.destroyInstance(instance, true);
    }

    @Test
    void testProfileServiceGetWinnerReturnsHighestScoringProfile(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player1 = env.createPlayer(instance);
        Player player2 = env.createPlayer(instance);

        ProfileService service = new ProfileService();
        assertNull(service.getWinner(), "Winner should be null when profile service is empty");

        BounceProfile profile1 = service.add(player1);
        BounceProfile profile2 = service.add(player2);

        profile1.addPoints(20);
        profile2.addPoints(5);

        BounceProfile winner = service.getWinner();
        assertNotNull(winner, "Winner profile should not be null");
        assertEquals(profile1, winner, "Winner should be the profile with the highest score");

        env.destroyInstance(instance, true);
    }
}
