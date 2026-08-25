package net.theevilreaper.bounce.common.permission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Pins the assumption every other test relies on: the LuckPerms loader is kept off the test class
 * path, so this module runs in its LuckPerms-free mode while tests execute.
 */
class LuckPermsSupportTest {

    @Test
    void testLoaderIsAbsentDuringTests() {
        assertFalse(LuckPermsSupport.isPresent(), "The test class path must not carry the LuckPerms loader");
    }

    @Test
    void testBootstrapIsSilentWithoutLoader() {
        assertDoesNotThrow(LuckPermsSupport::bootstrap);
    }

    @Test
    void testNoteFallbackGrantDoesNotThrowOnRepeatedCalls() {
        assertDoesNotThrow(() -> {
            LuckPermsSupport.noteFallbackGrant("bounce.test");
            LuckPermsSupport.noteFallbackGrant("bounce.test");
            LuckPermsSupport.noteFallbackGrant("bounce.other");
        });
    }

    @Test
    void testNoteBrokenProviderDoesNotThrowOnRepeatedCalls() {
        assertDoesNotThrow(() -> {
            LuckPermsSupport.noteBrokenProvider("bounce.test", new IllegalStateException("no provider"));
            LuckPermsSupport.noteBrokenProvider("bounce.test", new IllegalStateException("no provider"));
        });
    }
}
