package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.bounce.common.push.PushData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    void testGameMap() {
        GameMap gameMap = new GameMap("Test-Map", Pos.ZERO, new Pos(10, 0, 10), PushData.builder().build(), List.of());
        assertNotNull(gameMap);
        assertInstanceOf(BaseMap.class, gameMap, "The GameMap should be rely on the BaseMap");
        assertEquals("Test-Map", gameMap.name());
        assertEquals(Pos.ZERO, gameMap.spawn());
        assertEquals(new Pos(10, 0, 10), gameMap.getGameSpawn());
        assertNotEquals(gameMap.getGameSpawn(), gameMap.spawn());
        assertNotNull(gameMap.getPushData());
        assertTrue(gameMap.getPushData().push().isEmpty());
    }
}
