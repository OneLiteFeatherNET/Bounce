package net.theevilreaper.bounce.setup.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.world.DimensionType;
import net.onelitefeather.falco.anvil.FalcoAnvilLoader;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.provider.AbstractMapProvider;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.map.MapFilters;
import net.theevilreaper.bounce.common.util.GsonUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public final class SetupMapProvider extends AbstractMapProvider {

    private final FalcoAnvilLoader falcoAnvilLoader;
    
    public SetupMapProvider(Path path) {
        super(GsonUtil.GSON_FILE_HANDLER, MapFilters::filterMapsForSetup);
        this.loadMapEntries(path.resolve("maps"));

        Optional<MapEntry> fetchedEntry = this.mapEntries.stream()
                .filter(MapEntry::hasMapFile)
                .filter(mapEntry -> mapEntry.getDirectoryRoot().endsWith("lobby"))
                .findFirst();

        if (fetchedEntry.isEmpty()) {
            throw new IllegalStateException("Lobby map file not found!");
        }

        MapEntry lobbyEntry = fetchedEntry.get();
        this.mapEntries.remove(lobbyEntry);
        this.activeInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.falcoAnvilLoader = new FalcoAnvilLoader(lobbyEntry.getDirectoryRoot(), DimensionType.OVERWORLD.key());
        this.activeInstance.enableAutoChunkLoad(true);
        var defaultClock = this.activeInstance.defaultClock();
        if (defaultClock != null) {
            defaultClock.rate(0f);
        }
        MinecraftServer.getInstanceManager().registerInstance(this.activeInstance);
        Optional<BaseMap> loadedMap = this.fileHandler.load(lobbyEntry.getMapFile(), BaseMap.class);

        if (loadedMap.isEmpty()) {
            throw new IllegalStateException("Failed to load lobby map file: " + lobbyEntry.getMapFile());
        }

        this.activeMap = loadedMap.get();
    }
    
    public void cleanUp() {
        try {
            this.falcoAnvilLoader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMap(Path path, BaseMap baseMap) {
        this.fileHandler.save(path, baseMap instanceof GameMap gameMap ? gameMap : baseMap);
    }
}
