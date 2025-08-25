package net.theevilreaper.bounce.block.type.lantern;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class LanternBlockFactoryTest {

    @ParameterizedTest(name = "Test creation for lantern type {0}")
    @EnumSource(LanternBlockFactory.Type.class)
    void testLanternBlockHandlerCreation(LanternBlockFactory.Type type) {
        LanternBlockHandler lanternBlockHandler = LanternBlockFactory.create(type);
        assertNotNull(lanternBlockHandler, "LanternBlockHandler should not be null for type: " + type);
        assertEquals(type.getBlock().key(), lanternBlockHandler.getKey());
    }
}
