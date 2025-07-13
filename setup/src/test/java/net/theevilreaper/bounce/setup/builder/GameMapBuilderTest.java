package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapBuilderTest {

    @Test
    void testGameMapBuilderInitialization() {
        GameMapBuilder gameMapBuilder = new GameMapBuilder();
        assertNotNull(gameMapBuilder, "GameMapBuilder should not be null after initialization");

        PushData.Builder pushDataBuilder = gameMapBuilder.getPushDataBuilder();
        assertNotNull(pushDataBuilder, "PushDataBuilder should not be null after initialization");

         assertEquals(4, pushDataBuilder.getPushValues().size());

        PushEntry groundEntry = gameMapBuilder.getGroundBlockEntry();

        assertNotNull(groundEntry, "GroundBlockEntry should not be null after initialization");
        assertEquals(1, groundEntry.getValue());
        assertTrue(groundEntry.isGround());
        assertEquals(Block.GLASS, groundEntry.getBlock(), "Ground block should be GLASS");

         assertAll(
                 "Assert null values",
                 () -> assertNull(gameMapBuilder.getGameSpawn()),
                    () -> assertNull(gameMapBuilder.getName()),
                    () -> assertNull(gameMapBuilder.getSpawn())
         );

         assertTrue(gameMapBuilder.getAuthors().isEmpty());
    }

    @Test
    void testGameMapBuilderInitializationWithExistingData() {
        GameMapBuilder gameMapBuilder = new GameMapBuilder();
        gameMapBuilder.setGameSpawn(new Pos(1, 2, 3));
        gameMapBuilder.setName("Test Map");
        gameMapBuilder.setSpawn(new Pos(4, 5, 6));
        gameMapBuilder.addAuthor("Test");

        gameMapBuilder.getPushDataBuilder().getPushValues().get(2).setBlock(Block.STONE);

        GameMap gameMap = gameMapBuilder.build();

        assertNotNull(gameMap, "GameMap should not be null after initialization");
        assertEquals(new Pos(1, 2, 3), gameMap.getGameSpawn(), "Game spawn position should match");
        assertEquals("Test Map", gameMap.getName(), "Game map name should match");
        assertEquals(new Pos(4, 5, 6), gameMap.getSpawn(), "Spawn position should match");
        assertEquals(1, gameMap.getPushData().getPush(Block.STONE), "Push value for STONE should be 1");

        GameMapBuilder anotherBuilder = new GameMapBuilder(gameMap);

        assertNotNull(anotherBuilder, "GameMapBuilder should not be null after initialization with existing data");
        assertEquals(gameMap.getGameSpawn(), anotherBuilder.getGameSpawn(), "Game spawn position should match");
        assertEquals(gameMap.getName(), anotherBuilder.getName(), "Game map name should match");
        assertEquals(gameMap.getSpawn(), anotherBuilder.getSpawn(), "Spawn position should match");

        PushData.Builder anotherPushDataBuilder = anotherBuilder.getPushDataBuilder();
        assertNotNull(anotherPushDataBuilder, "PushDataBuilder should not be null after initialization with existing data");
        PushEntry secondEntry = anotherPushDataBuilder.getPushValues().get(2);
        assertNotNull(secondEntry, "Push entry for STONE should not be null");

        assertEquals(Block.STONE, secondEntry.getBlock(), "Push value for STONE should match");
        // TODO: Fix me later
        //assertTrue(anotherBuilder.getAuthors().contains("Test"), "Authors should contain 'Test'");
        assertEquals(4, anotherBuilder.getPushDataBuilder().getPushValues().size(), "Push data should contain four entries");
    }
}