package net.theevilreaper.bounce.common.bootstrap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceBootstrapTest {

    private String originalBindHost;
    private String originalBindPort;
    private String originalWorkingDir;

    @BeforeEach
    void captureSystemProperties() {
        originalBindHost = System.getProperty("service.bind.host");
        originalBindPort = System.getProperty("service.bind.port");
        originalWorkingDir = System.getProperty("service.working.dir");
    }

    @AfterEach
    void restoreSystemProperties() {
        restoreOrClear("service.bind.host", originalBindHost);
        restoreOrClear("service.bind.port", originalBindPort);
        restoreOrClear("service.working.dir", originalWorkingDir);
    }

    private static void restoreOrClear(String key, String originalValue) {
        if (originalValue == null) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, originalValue);
        }
    }

    @Test
    @DisabledIfSystemProperty(named = "service.bind.host", matches = ".+")
    void testDefaultBindHost() {
        assertEquals("localhost", ServiceBootstrap.resolveBindHost());
    }

    @Test
    @DisabledIfSystemProperty(named = "service.bind.port", matches = ".+")
    void testDefaultBindPort() {
        assertEquals(25565, ServiceBootstrap.resolveBindPort());
    }

    @Test
    void testBindHostFromSystemProperty() {
        System.setProperty("service.bind.host", "0.0.0.0");
        assertEquals("0.0.0.0", ServiceBootstrap.resolveBindHost());
    }

    @Test
    void testBindPortFromSystemProperty() {
        System.setProperty("service.bind.port", "30000");
        assertEquals(30000, ServiceBootstrap.resolveBindPort());
    }

    @Test
    @DisabledIfSystemProperty(named = "service.working.dir", matches = ".+")
    void testDefaultWorkingDirectory() {
        assertEquals(Paths.get(""), ServiceBootstrap.resolveWorkingDirectory());
    }

    @Test
    void testWorkingDirectoryFromSystemProperty() {
        System.setProperty("service.working.dir", "/app");
        assertEquals(Paths.get("/app"), ServiceBootstrap.resolveWorkingDirectory());
    }
}
