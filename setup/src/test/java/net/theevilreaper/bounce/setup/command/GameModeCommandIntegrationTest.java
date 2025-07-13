package net.theevilreaper.bounce.setup.command;

import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class GameModeCommandIntegrationTest {

    @Test
    void testValidGameModeCommandUsage(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        assertEquals(GameMode.SURVIVAL, player.getGameMode());

        GameModeCommand gameModeCommand = new GameModeCommand();
        CommandManager commandManager = env.process().command();
        assertTrue(commandManager.getCommands().isEmpty());

        commandManager.register(gameModeCommand);
        assertFalse(commandManager.getCommands().isEmpty());
        assertEquals(1, commandManager.getCommands().size());

        assertGameModeSwitch(env, commandManager, player, GameMode.CREATIVE);
        assertGameModeSwitch(env, commandManager, player, GameMode.SPECTATOR);

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
        commandManager.unregister(gameModeCommand);
        assertTrue(commandManager.getCommands().isEmpty(), "Command manager should be empty after unregistering the command");
    }

    /**
     * Asserts that the player's game mode is switched to the specified game mode after executing the command.
     *
     * @param env      the environment for the test
     * @param manager  the command manager to execute the command
     * @param player   the player whose game mode is being tested
     * @param gameMode the game mode to switch to
     */
    private void assertGameModeSwitch(@NotNull Env env, @NotNull CommandManager manager, @NotNull Player player, @NotNull GameMode gameMode) {
        String parsedMode = gameMode.name().toLowerCase();
        manager.execute(player, "gamemode " + parsedMode);
        env.tick();
        assertEquals(gameMode, player.getGameMode(), "Player's game mode should be set to " + parsedMode);
    }
}
