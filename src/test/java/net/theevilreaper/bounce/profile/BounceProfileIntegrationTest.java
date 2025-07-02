package net.theevilreaper.bounce.profile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.SystemChatPacket;
import net.minestom.testing.Collector;
import net.minestom.testing.Env;
import net.minestom.testing.FlexibleListener;
import net.minestom.testing.TestConnection;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class BounceProfileIntegrationTest {

    @Test
    void testBounceProfileCreation(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        BounceProfile bounceProfile = new BounceProfile(player);

        assertNotNull(bounceProfile);
        assertEquals(player.getUuid(), bounceProfile.getPlayer().getUuid());
        assertNull(bounceProfile.getLastDamager());
        assertNull(bounceProfile.getJumpRunnable());

        Player damager = env.createPlayer(instance);

        bounceProfile.setLastDamager(damager);

        assertNotNull(bounceProfile.getLastDamager(), "Last damager should not be null after setting");
        assertEquals(damager.getUuid(), bounceProfile.getLastDamager().getUuid());
        assertNotEquals(player.getUuid(), bounceProfile.getLastDamager().getUuid());

        bounceProfile.resetDamager();
        assertNull(bounceProfile.getLastDamager(), "Last damager should be null after setting to null");

        assertNull(bounceProfile.getJumpRunnable());

        bounceProfile.registerJumpRunnable(PushData.builder().build());

        assertNotNull(bounceProfile.getJumpRunnable());

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    @Test
    void testBounceProfilePointsAdd(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        BounceProfile bounceProfile = new BounceProfile(player);

        assertNotNull(bounceProfile);

        FlexibleListener<ScoreUpdateEvent> listen = env.listen(ScoreUpdateEvent.class);

        listen.followup(event -> {
            assertEquals(player.getUuid(), event.getPlayer().getUuid());
            assertEquals(10, event.getPoints());
        });

        bounceProfile.addPoints(10);

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    @Test
    void testBounceProfilePointsRemove(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        BounceProfile bounceProfile = new BounceProfile(player);
        bounceProfile.addPoints(5); // Start with 20 points

        assertNotNull(bounceProfile);

        FlexibleListener<ScoreUpdateEvent> listen = env.listen(ScoreUpdateEvent.class);

        listen.followup(event -> {
            assertEquals(player.getUuid(), event.getPlayer().getUuid());
            assertEquals(0, event.getPoints());
        });

        // The given profile has 5 points, so removing 5 should set it to zero
        bounceProfile.removePoints(5);

        Collector<ScoreUpdateEvent> scoreUpdateEventCollector = env.trackEvent(ScoreUpdateEvent.class, EventFilter.PLAYER, player);
        // Remove 5 points
        bounceProfile.removePoints(5);

        scoreUpdateEventCollector.assertEmpty();

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    @Test
    void testBounceProfileStatsPrint(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        TestConnection connection = env.createConnection();
        Player player = connection.connect(instance, Pos.ZERO);
        BounceProfile bounceProfile = new BounceProfile(player);

        for (int i = 0; i < 2; i++) {
            bounceProfile.addPoints(10);
            bounceProfile.addDeath();
            bounceProfile.addKill();
        }

        Collector<SystemChatPacket> noWinMessagePacket = connection.trackIncoming(SystemChatPacket.class);

        bounceProfile.sendStats(false);

        noWinMessagePacket.assertSingle(systemChatPacket -> {
            assertNotNull(systemChatPacket.message());
            List<Component> components = flattenComponents(systemChatPacket.message());

            assertEntry("Points", "20", components);
            assertEntry("Kills", "4", components);
            assertEntry("Deaths", "2", components);
            assertEntry("Win", "✘", components);
        });

        Collector<SystemChatPacket> winMessagePacket = connection.trackIncoming(SystemChatPacket.class);

        bounceProfile.sendStats(true);

        winMessagePacket.assertSingle(systemChatPacket -> {
            assertNotNull(systemChatPacket.message());
            List<Component> components = flattenComponents(systemChatPacket.message());

            assertEntry("Points", "20", components);
            assertEntry("Kills", "4", components);
            assertEntry("Deaths", "2", components);
            assertEntry("Win", "✔", components);
        });

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    private void assertEntry(@NotNull String search, @NotNull String expected, @NotNull List<Component> message) {
        AbstractMap.SimpleEntry<Component, Component> components = findLabelAndValue(message, search);
        assertNotNull(components);
        assertComponent(search, components.getKey());
        assertComponent(expected, components.getValue());
    }

    /**
     * Asserts that the serialized component contains the expected string.
     *
     * @param expected  the expected string to be contained in the serialized component
     * @param component the component to be serialized and checked
     */
    private void assertComponent(@NotNull String expected, @NotNull Component component) {
        String serialized = plainText().serialize(component);
        assertNotNull(serialized, "Serialized component should not be null");
        assertFalse(serialized.isEmpty(), "Serialized component should not be empty");
        assertTrue(serialized.contains(expected), "Serialized component should contain: " + expected);
    }

    /**
     * Flattens the component and its children into a list.
     *
     * @param component the component to flatten
     * @return a list of components
     */
    private @NotNull List<Component> flattenComponents(@NotNull Component component) {
        List<Component> result = new ArrayList<>();
        for (Component child : component.children()) {
            result.add(child);
            result.addAll(flattenComponents(child));
        }
        return result;
    }

    /**
     * Finds a label and its corresponding value in the list of components.
     * @param components the list of components to search through
     * @param label the label to find
     * @return an entry containing the label component and its value component, or null if not found
     */
    private @Nullable AbstractMap.SimpleEntry<Component, Component> findLabelAndValue(
            @NotNull List<Component> components,
            @NotNull String label
    ) {
        for (int i = 0; i < components.size() - 1; i++) {
            Component current = components.get(i);
            String decoded = plainText().serialize(current);
            if (decoded.contains(label)) {
                return new AbstractMap.SimpleEntry<>(current, current.children().getFirst());
            }
        }
        return null;
    }
}