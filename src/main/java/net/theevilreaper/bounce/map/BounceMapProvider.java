package net.theevilreaper.bounce.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.map.AbstractMapProvider;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.map.MapFilters;
import net.theevilreaper.bounce.common.util.GsonUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

public class BounceMapProvider extends AbstractMapProvider {

    public BounceMapProvider(@NotNull Path path) {
        super(new GsonFileHandler(GsonUtil.GSON), MapFilters::filterMapsForGame);
        this.mapEntries = this.loadMapEntries(path.resolve("maps"));
        this.activeInstance = MinecraftServer.getInstanceManager().createInstanceContainer();

        MapEntry mapEntry = this.getEntries().getFirst();

        if (mapEntry == null) {
            throw new IllegalStateException("No map found in the available maps");
        }

        Optional<GameMap> loadedDataMap = this.fileHandler.load(mapEntry.getMapFile(), GameMap.class);

        if (loadedDataMap.isEmpty()) {
            throw new  IllegalStateException("An error occurred while loading the map");
        }

        this.activeMap = loadedDataMap.get();
        this.registerInstance(this.activeInstance, mapEntry);

    }

    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        throw new  UnsupportedOperationException("Not supported yet.");
    }

    public void teleportToGameSpawn(@NotNull Player player) {
        player.teleport(((GameMap) this.activeMap).getGameSpawn());
    }

    public String getMapName() {
        return this.activeMap.getName();
    }

    public @NotNull GameMap getActiveMap() {
        if (!(this.activeMap instanceof GameMap gameMap)) {
            throw new IllegalStateException("Active map is not a GameMap");
        }
        return gameMap;
    }
}
