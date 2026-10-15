package net.theevilreaper.bounce.common.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InternalGameConfigTest {

    @Test
    void testInternalConfig() {
        GameConfig gameConfig = InternalGameConfig.defaultConfig();
        assertNotNull(gameConfig);
        assertInstanceOf(InternalGameConfig.class, gameConfig);
    }

    @Test
    void testDefaultGameTimeIsFiveMinutes() {
        assertEquals(300, InternalGameConfig.defaultConfig().gameTime(), "A round should last five minutes by default");
    }

    @Test
    void testSentryIsOffByDefault() {
        assertNull(InternalGameConfig.defaultConfig().sentryDsn(), "A local run must not report to Sentry");
    }
}
