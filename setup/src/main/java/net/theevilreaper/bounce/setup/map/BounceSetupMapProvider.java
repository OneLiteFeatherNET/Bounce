package net.theevilreaper.bounce.setup.map;

import net.minestom.server.MinecraftServer;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.provider.AbstractMapProvider;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.map.MapFilters;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

public final class BounceSetupMapProvider extends AbstractMapProvider {

    public BounceSetupMapProvider(@NotNull FileHandler fileHandler, @NotNull Path path) {
        super(fileHandler, MapFilters::filterMapsForSetup);
        this.mapEntries = loadMapEntries(path.resolve("maps"));

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
        this.registerInstance(this.activeInstance, lobbyEntry);

        Optional<BaseMap> loadedMap = this.fileHandler.load(lobbyEntry.getMapFile(), BaseMap.class);

        if (loadedMap.isEmpty()) {
            throw new IllegalStateException("Failed to load lobby map file: " + lobbyEntry.getMapFile());
        }

        this.activeMap = loadedMap.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        this.fileHandler.save(path, baseMap instanceof GameMap gameMap ? gameMap : baseMap);
    }
}
