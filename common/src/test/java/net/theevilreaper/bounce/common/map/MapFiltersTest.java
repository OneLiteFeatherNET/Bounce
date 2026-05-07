package net.theevilreaper.bounce.common.map;

import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.config.GameConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MapFiltersTest {

    @TempDir
    Path tempDir;

    @Test
    void testFilterMapsForGameReturnsValidMap() throws IOException {
        Path map = tempDir.resolve("map1");
        Files.createDirectory(map);
        Files.createDirectory(map.resolve(MapFilters.REGION_FOLDER));
        Files.createFile(map.resolve(GameConfig.MAP_FILE));

        List<MapEntry> result = MapFilters.filterMapsForGame(Stream.of(map));
        assertEquals(1, result.size());
    }

    @Test
    void testFilterMapsForGameExcludesMissingRegionFolder() throws IOException {
        Path map = tempDir.resolve("map_no_region");
        Files.createDirectory(map);
        Files.createFile(map.resolve(GameConfig.MAP_FILE));

        List<MapEntry> result = MapFilters.filterMapsForGame(Stream.of(map));
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterMapsForGameExcludesMissingMapFile() throws IOException {
        Path map = tempDir.resolve("map_no_config");
        Files.createDirectory(map);
        Files.createDirectory(map.resolve(MapFilters.REGION_FOLDER));

        List<MapEntry> result = MapFilters.filterMapsForGame(Stream.of(map));
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterMapsForGameExcludesFiles() throws IOException {
        Path file = tempDir.resolve("not_a_directory.txt");
        Files.createFile(file);

        List<MapEntry> result = MapFilters.filterMapsForGame(Stream.of(file));
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterMapsForSetupReturnsValidMap() throws IOException {
        Path map = tempDir.resolve("map1");
        Files.createDirectory(map);
        Files.createDirectory(map.resolve(MapFilters.REGION_FOLDER));

        List<MapEntry> result = MapFilters.filterMapsForSetup(Stream.of(map));
        assertEquals(1, result.size());
    }

    @Test
    void testFilterMapsForSetupExcludesMissingRegionFolder() throws IOException {
        Path map = tempDir.resolve("map_no_region");
        Files.createDirectory(map);

        List<MapEntry> result = MapFilters.filterMapsForSetup(Stream.of(map));
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterMapsForSetupDoesNotRequireMapFile() throws IOException {
        Path map = tempDir.resolve("map_no_config");
        Files.createDirectory(map);
        Files.createDirectory(map.resolve(MapFilters.REGION_FOLDER));

        List<MapEntry> result = MapFilters.filterMapsForSetup(Stream.of(map));
        assertEquals(1, result.size());
    }

    @Test
    void testEmptyStreamReturnsEmptyList() {
        assertTrue(MapFilters.filterMapsForGame(Stream.empty()).isEmpty());
        assertTrue(MapFilters.filterMapsForSetup(Stream.empty()).isEmpty());
    }
}