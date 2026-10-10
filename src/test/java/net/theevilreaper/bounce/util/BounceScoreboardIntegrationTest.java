package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class BounceScoreboardIntegrationTest {

    @Test
    void testMarkPlayerLeftStrikesThroughTheLine(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        player.setDisplayName(Component.text(player.getUsername()));

        BounceScoreboard scoreboard = new BounceScoreboard();
        scoreboard.createPlayerLine(player);
        scoreboard.updatePlayerLine(player.getUuid(), 15);

        scoreboard.markPlayerLeft(player);

        Sidebar.ScoreboardLine line = scoreboard.getPlayerLine(player.getUuid());
        assertNotNull(line);
        assertTrue(line.getContent().hasDecoration(TextDecoration.STRIKETHROUGH), "The line of a player who left must be struck through");
        assertEquals(15, line.getLine(), "The score of a player who left must stay visible");

        env.destroyInstance(instance, true);
    }

    @Test
    void testMarkPlayerLeftWithoutLineIsANoOp(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        BounceScoreboard scoreboard = new BounceScoreboard();

        assertDoesNotThrow(() -> scoreboard.markPlayerLeft(player));
        assertNull(scoreboard.getPlayerLine(player.getUuid()));

        env.destroyInstance(instance, true);
    }
}
