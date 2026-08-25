package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                    () -> assertNull(gameMapBuilder.getSpawn())
         );

         assertTrue(gameMapBuilder.getBuilders().isEmpty());
    }

    @Test
    void testGameMapBuilderInitializationWithExistingData() {
        GameMapBuilder gameMapBuilder = new GameMapBuilder();
        gameMapBuilder.gameSpawn(new Pos(1, 2, 3));
        gameMapBuilder.name("Test Map");
        gameMapBuilder.spawn(new Pos(4, 5, 6));
        gameMapBuilder.builder("Test");

        gameMapBuilder.getPushDataBuilder().getPushValues().get(2).setBlock(Block.STONE);

        GameMap gameMap = gameMapBuilder.build();

        assertNotNull(gameMap, "GameMap should not be null after initialization");
        assertEquals(new Pos(1, 2, 3), gameMap.getGameSpawn(), "Game spawn position should match");
        assertEquals("Test Map", gameMap.name(), "Game map name should match");
        assertEquals(new Pos(4, 5, 6), gameMap.spawn(), "Spawn position should match");
        assertEquals(1, gameMap.getPushData().getPush(Block.STONE), "Push value for STONE should be 1");

        GameMapBuilder anotherBuilder = new GameMapBuilder(gameMap);

        assertNotNull(anotherBuilder, "GameMapBuilder should not be null after initialization with existing data");
        assertEquals(gameMap.getGameSpawn(), anotherBuilder.getGameSpawn(), "Game spawn position should match");
        assertEquals(gameMap.name(), anotherBuilder.getName(), "Game map name should match");
        assertEquals(gameMap.spawn(), anotherBuilder.getSpawn(), "Spawn position should match");

        PushData.Builder anotherPushDataBuilder = anotherBuilder.getPushDataBuilder();
        assertNotNull(anotherPushDataBuilder, "PushDataBuilder should not be null after initialization with existing data");
        PushEntry secondEntry = anotherPushDataBuilder.getPushValues().get(2);
        assertNotNull(secondEntry, "Push entry for STONE should not be null");

        assertEquals(Block.STONE, secondEntry.getBlock(), "Push value for STONE should match");
        // TODO: Fix me later
        //assertTrue(anotherBuilder.getAuthors().contains("Test"), "Authors should contain 'Test'");
        assertEquals(4, anotherBuilder.getPushDataBuilder().getPushValues().size(), "Push data should contain four entries");
    }

    @Test
    void testNewBuilderHasNoAreaAndDefaultInterval() {
        GameMapBuilder builder = new GameMapBuilder();
        assertNull(builder.getArea());
        assertTrue(builder.getShuffleIntervalTicks() > 0, "A newly created map should have a sane default interval");
        assertTrue(builder.getReshufflePercentage() > 0, "A newly created map should have a sane default reshuffle percentage");
    }

    @Test
    void testAreaAndShuffleIntervalRoundTripThroughBuild() {
        GameMapBuilder builder = new GameMapBuilder();
        Area area = new GroundArea(Vec.ZERO, new Vec(5, 0, 5), Block.GLASS, PushData.builder().build());

        builder.area(area).shuffleIntervalTicks(60).reshufflePercentage(0.4);

        assertEquals(area, builder.getArea());
        assertEquals(60, builder.getShuffleIntervalTicks());
        assertEquals(0.4, builder.getReshufflePercentage());

        GameMap built = builder.build();
        assertEquals(area, built.getArea());
        assertEquals(60, built.getShuffleIntervalTicks());
        assertEquals(0.4, built.getReshufflePercentage());
    }

    @Test
    void testReloadingExistingMapWithoutAreaKeepsDefaultInterval() {
        GameMap gameMap = new GameMapBuilder().build();
        GameMapBuilder reloaded = new GameMapBuilder(gameMap);

        assertNull(reloaded.getArea());
        assertTrue(reloaded.getShuffleIntervalTicks() > 0);
        assertTrue(reloaded.getReshufflePercentage() > 0);
    }

    @Test
    void testNewBuilderHasNoAreaCorners() {
        GameMapBuilder builder = new GameMapBuilder();
        assertNull(builder.getPos1());
        assertNull(builder.getPos2());
    }

    @Test
    void testAreaCornersRoundTripThroughSetters() {
        GameMapBuilder builder = new GameMapBuilder();
        Vec pos1 = new Vec(1, 2, 3);
        Vec pos2 = new Vec(4, 5, 6);

        builder.pos1(pos1).pos2(pos2);

        assertEquals(pos1, builder.getPos1());
        assertEquals(pos2, builder.getPos2());
    }

    @Test
    void testReloadingExistingMapWithAreaRestoresCorners() {
        GameMapBuilder builder = new GameMapBuilder();
        Area area = new GroundArea(new Vec(1, 2, 3), new Vec(4, 5, 6), Block.GLASS, PushData.builder().build());
        builder.area(area);

        GameMap gameMap = builder.build();
        GameMapBuilder reloaded = new GameMapBuilder(gameMap);

        assertEquals(area.min(), reloaded.getPos1());
        assertEquals(area.max(), reloaded.getPos2());
    }

    @Test
    void testReloadingExistingMapWithoutAreaHasNoCorners() {
        GameMap gameMap = new GameMapBuilder().build();
        GameMapBuilder reloaded = new GameMapBuilder(gameMap);

        assertNull(reloaded.getPos1());
        assertNull(reloaded.getPos2());
    }

    @Test
    void testNewBuilderIsNotReadyToSave() {
        GameMapBuilder builder = new GameMapBuilder();

        assertFalse(builder.isReadyToSave());
        assertEquals(List.of("Name", "Spawn", "Game Spawn", "Area"), builder.getMissingFieldNames());
    }

    @Test
    void testBuilderIsReadyToSaveOnceAllRequiredFieldsAreSet() {
        GameMapBuilder builder = new GameMapBuilder();
        Area area = new GroundArea(Vec.ZERO, new Vec(5, 0, 5), Block.GLASS, PushData.builder().build());

        builder.name("Test Map");
        builder.spawn(new Pos(1, 2, 3));
        builder.gameSpawn(new Pos(4, 5, 6));
        builder.area(area);

        assertTrue(builder.getMissingFieldNames().isEmpty());
        assertTrue(builder.isReadyToSave());
    }

    @Test
    void testBuilderIsNotReadyToSaveWhenOnlySomeFieldsAreSet() {
        GameMapBuilder builder = new GameMapBuilder();
        builder.name("Test Map").spawn(new Pos(1, 2, 3));

        assertEquals(List.of("Game Spawn", "Area"), builder.getMissingFieldNames());
        assertFalse(builder.isReadyToSave());
    }

    private GameMapBuilder readyBuilderExceptPushData() {
        GameMapBuilder builder = new GameMapBuilder();
        Area area = new GroundArea(Vec.ZERO, new Vec(5, 0, 5), Block.GLASS, PushData.builder().build());
        builder.name("Test Map");
        builder.spawn(new Pos(1, 2, 3));
        builder.gameSpawn(new Pos(4, 5, 6));
        builder.area(area);
        return builder;
    }

    @Test
    void testBuilderIsNotReadyToSaveWhenNoPushEntryHasWeight() {
        GameMapBuilder builder = readyBuilderExceptPushData();
        for (PushEntry entry : builder.getPushDataBuilder().getPushValues()) {
            if (!entry.isGround()) entry.setWeight(0.0);
        }

        assertEquals(List.of("Push Data"), builder.getMissingFieldNames());
        assertFalse(builder.isReadyToSave());
    }

    @Test
    void testBuilderIsNotReadyToSaveWhenAnEntryHasNoValue() {
        GameMapBuilder builder = readyBuilderExceptPushData();
        builder.getPushDataBuilder().getPushValues().get(1).setValue(0);

        assertEquals(List.of("Push Data"), builder.getMissingFieldNames());
        assertFalse(builder.isReadyToSave());
    }
}