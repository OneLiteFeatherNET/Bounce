package net.theevilreaper.bounce.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
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

public class BounceMapProvider extends AbstractMapProvider {

    private final FalcoAnvilLoader falcoAnvilLoader;

    public BounceMapProvider(Path path) {
        super(GsonUtil.GSON_FILE_HANDLER, MapFilters::filterMapsForGame);
        this.loadMapEntries(path.resolve("maps"));
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
        this.falcoAnvilLoader = new FalcoAnvilLoader(mapEntry.getDirectoryRoot(), DimensionType.OVERWORLD.key());
        this.activeInstance.setChunkLoader(this.falcoAnvilLoader);
        this.activeInstance.enableAutoChunkLoad(true);
        var defaultClock = this.activeInstance.defaultClock();
        if (defaultClock != null) {
            defaultClock.rate(0f);
        }
        MinecraftServer.getInstanceManager().registerInstance(this.activeInstance);
    }

    @Override
    public void saveMap(Path path, BaseMap baseMap) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void teleportToGameSpawn(Player player) {
        player.teleport(((GameMap) this.activeMap).getGameSpawn());
    }

    public String getMapName() {
        return this.activeMap.name();
    }

    public void cleanUp() {
        try {
            this.falcoAnvilLoader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameMap getActiveMap() {
        if (!(this.activeMap instanceof GameMap gameMap)) {
            throw new IllegalStateException("Active map is not a GameMap");
        }
        return gameMap;
    }
}
