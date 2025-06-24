package net.theevilreaper.bounce.setup.data;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.onelitefeather.guira.data.BaseSetupData;
import net.onelitefeather.guira.event.SetupFinishEvent;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.inventory.ground.GroundViewInventory;
import net.theevilreaper.bounce.setup.inventory.ground.GroundLayerInventory;
import net.theevilreaper.bounce.setup.inventory.overview.MapOverviewInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

public final class BounceData extends BaseSetupData<GameMap> {

    private static final Pos SPAWN_POINT = new Pos(0, 100, 0);
    private final FileHandler fileHandler;
    private final Player player;

    private InstanceContainer instance;
    private GameMapBuilder gameMapBuilder;
    private MapOverviewInventory overviewInventory;
    private GroundViewInventory groundViewInventory;
    private GroundLayerInventory groundLayerInventory;

    public BounceData(@NotNull UUID owner, @NotNull MapEntry mapEntry, @NotNull FileHandler fileHandler) {
        super(owner, mapEntry);
        this.fileHandler = fileHandler;
        Player foundPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(owner);

        if (foundPlayer == null) {
            throw new IllegalArgumentException("Player with UUID " + owner + " is not online.");
        }

        this.player = foundPlayer;
        this.loadData();
    }

    public void openInventory() {
        this.overviewInventory.open();
    }

    public void triggerUpdate() {
        this.overviewInventory.invalidateDataLayout();
    }

    public void teleport(@NotNull Player player) {
        Pos spawnPoint = this.gameMapBuilder.getSpawnOrDefault(SPAWN_POINT);
        player.setInstance(this.instance, spawnPoint);
    }

    @Override
    public void save() {
        if (!Files.exists(mapEntry.getMapFile())) {
            this.mapEntry.createFile();
        }
        this.fileHandler.save(mapEntry.getDirectoryRoot(), map);
        EventDispatcher.call(new SetupFinishEvent<>(this));
    }

    @Override
    public void reset() {
        if (this.overviewInventory != null) {
            this.overviewInventory.unregister();
        }
    }

    @Override
    public void loadData() {
        Optional<GameMap> mapData = this.fileHandler.load(mapEntry.getMapFile(), GameMap.class);
        // Initialize with a new BaseMap if loading fails
        mapData.ifPresentOrElse(gameMap -> {
            this.map = gameMap;
            this.gameMapBuilder = new GameMapBuilder(gameMap);
        }, () -> this.gameMapBuilder = new GameMapBuilder());

        this.groundViewInventory = new GroundViewInventory(this.player, this.gameMapBuilder, () -> this.groundLayerInventory.ge);
        this.groundViewInventory.register();

        this.groundLayerInventory = new GroundLayerInventory(this.groundViewInventory::open);
        this.groundLayerInventory.register();

        this.overviewInventory = new MapOverviewInventory(this.player, this.gameMapBuilder, this.groundViewInventory::open);
        this.overviewInventory.register();


        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        AnvilLoader anvilLoader = new AnvilLoader(this.mapEntry.getDirectoryRoot());
        this.instance.setChunkLoader(anvilLoader);

        MinecraftServer.getInstanceManager().registerInstance(this.instance);
    }

    public @Nullable GameMapBuilder getMapBuilder() {
        return this.gameMapBuilder;
    }

}
