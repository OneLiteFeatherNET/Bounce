package net.theevilreaper.bounce.setup.map;

import net.minestom.server.MinecraftServer;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.provider.AbstractMapProvider;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.map.MapFilters;
import net.theevilreaper.bounce.common.util.GsonUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

public final class BounceSetupMapProvider extends AbstractMapProvider {

    public BounceSetupMapProvider(@NotNull Path path) {
        super(new GsonFileHandler(GsonUtil.GSON), MapFilters::filterMapsForSetup);
        this.mapEntries = loadMapEntries(path.resolve("maps"));

        Optional<MapEntry> fetchedEntry = this.mapEntries.stream()
                .filter(MapEntry::hasMapFile)
                .filter(mapEntry -> mapEntry.getDirectoryRoot().endsWith("lobby"))
                .findFirst();

        if (fetchedEntry.isEmpty()) {
            throw new RuntimeException("Lobby map file not found!");
        }

        MapEntry lobbyEntry = fetchedEntry.get();
        this.mapEntries.remove(lobbyEntry);
        this.activeInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.registerInstance(this.activeInstance, lobbyEntry);

        Optional<BaseMap> loadedMap = this.fileHandler.load(lobbyEntry.getMapFile(), BaseMap.class);

        if (loadedMap.isEmpty()) {
            throw new RuntimeException("Failed to load lobby map file: " + lobbyEntry.getMapFile());
        }

        this.activeMap = loadedMap.get();
    }

    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        this.fileHandler.save(path, baseMap instanceof GameMap gameMap ? gameMap : baseMap);
    }
}
