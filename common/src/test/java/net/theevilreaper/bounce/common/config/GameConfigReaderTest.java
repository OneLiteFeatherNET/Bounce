package net.theevilreaper.bounce.common.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void testMissingFileReturnsDefaultConfig() {
        GameConfig config = new GameConfigReader(tempDir.resolve("nonexistent")).getConfig();
        assertEquals(InternalGameConfig.defaultConfig(), config);
    }

    @Test
    void testEmptyFileReturnsDefaultConfig() throws IOException {
        Files.createFile(tempDir.resolve("config.properties"));
        GameConfig config = new GameConfigReader(tempDir).getConfig();
        assertEquals(InternalGameConfig.defaultConfig(), config);
    }

    @Test
    void testValidConfigFile() throws IOException {
        Files.writeString(tempDir.resolve("config.properties"), """
                minPlayers=2
                maxPlayers=8
                lobbyTime=30
                gameTime=300
                """);
        GameConfig config = new GameConfigReader(tempDir).getConfig();
        assertAll(
                () -> assertEquals(2, config.minPlayers()),
                () -> assertEquals(8, config.maxPlayers()),
                () -> assertEquals(30, config.lobbyTime()),
                () -> assertEquals(300, config.gameTime())
        );
    }

    @Test
    void testPartialConfigFallsBackToDefaults() throws IOException {
        Files.writeString(tempDir.resolve("config.properties"), "minPlayers=2\n");
        GameConfig config = new GameConfigReader(tempDir).getConfig();
        GameConfig defaults = InternalGameConfig.defaultConfig();
        assertAll(
                () -> assertEquals(2, config.minPlayers()),
                () -> assertEquals(defaults.maxPlayers(), config.maxPlayers()),
                () -> assertEquals(defaults.lobbyTime(), config.lobbyTime()),
                () -> assertEquals(defaults.gameTime(), config.gameTime())
        );
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testUnreadableFileReturnsDefaultConfig() throws IOException {
        Path config = tempDir.resolve("config.properties");
        Files.writeString(config, "minPlayers=2\n");
        config.toFile().setReadable(false);

        GameConfig result = new GameConfigReader(tempDir).getConfig();
        assertEquals(InternalGameConfig.defaultConfig(), result);
    }
}
