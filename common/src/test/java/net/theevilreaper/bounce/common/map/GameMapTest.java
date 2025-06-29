package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.bounce.common.push.PushData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    void testGameMap() {
        GameMap gameMap = new GameMap("Test-Map", Pos.ZERO, new Pos(10, 0, 10), PushData.builder().build());
        assertNotNull(gameMap);
        assertInstanceOf(BaseMap.class, gameMap, "The GameMap should be rely on the BaseMap");
        assertEquals("Test-Map", gameMap.getName());
        assertEquals(Pos.ZERO, gameMap.getSpawn());
        assertEquals(new Pos(10, 0, 10), gameMap.getGameSpawn());
        assertNotEquals(gameMap.getGameSpawn(), gameMap.getSpawn());
        assertNotNull(gameMap.getPushData());
        assertTrue(gameMap.getPushData().push().isEmpty());
    }
}
