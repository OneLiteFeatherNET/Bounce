package net.theevilreaper.bounce.setup.inventory;

import net.theevilreaper.aves.map.MapEntry;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * This class can be used for each test or integration test which relies on the {@link MapEntry} class.
 * It provides a static instance of the {@link MapEntry} class which can be used in the tests.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class MapDataTestBase {

    protected static final MapEntry testMapEntry;

    static {
        ClassLoader classLoader = MapDataTestBase.class.getClassLoader();
        URI folderUri;
        try {
            folderUri = classLoader.getResource("testMap").toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        testMapEntry = MapEntry.of(Paths.get(folderUri));
    }
}
