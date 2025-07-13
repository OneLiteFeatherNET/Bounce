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
import net.theevilreaper.bounce.setup.inventory.overview.MapOverviewInventory;
import net.theevilreaper.bounce.setup.inventory.push.PushValueInventory;
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
    private PushValueInventory pushValueInventory;

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

    public void teleport(@NotNull Player player) {
        Pos spawnPoint = this.gameMapBuilder.getSpawnOrDefault(SPAWN_POINT);
        player.setInstance(this.instance, spawnPoint);
    }

    @Override
    public void save() {
        if (!Files.exists(mapEntry.getMapFile())) {
            this.mapEntry.createFile();
        }
        this.map = this.gameMapBuilder.build();
        this.fileHandler.save(mapEntry.getMapFile(), map);
        EventDispatcher.call(new SetupFinishEvent<>(this));
    }

    @Override
    public void reset() {
        if (this.overviewInventory != null) {
            this.overviewInventory.unregister();
        }

        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            MinecraftServer.getInstanceManager().unregisterInstance(this.instance);
        });
    }

    @Override
    public void loadData() {
        if (this.mapEntry.getMapFile() == null) {
            this.gameMapBuilder = new GameMapBuilder();
        } else {
            Optional<GameMap> mapData = this.fileHandler.load(mapEntry.getMapFile(), GameMap.class);
            // Initialize with a new BaseMap if loading fails
            mapData.ifPresentOrElse(gameMap -> {
                this.map = gameMap;
                this.gameMapBuilder = new GameMapBuilder(gameMap);
            }, () -> this.gameMapBuilder = new GameMapBuilder());
        }

        this.groundViewInventory = new GroundViewInventory(this.player, this.gameMapBuilder);
        this.groundViewInventory.register();

        this.overviewInventory = new MapOverviewInventory(this.player, this.gameMapBuilder);
        this.overviewInventory.register();

        this.pushValueInventory = new PushValueInventory(this.player, this.gameMapBuilder);
        this.pushValueInventory.register();

        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        AnvilLoader anvilLoader = new AnvilLoader(this.mapEntry.getDirectoryRoot());
        this.instance.setChunkLoader(anvilLoader);

        MinecraftServer.getInstanceManager().registerInstance(this.instance);
    }

    public @Nullable GameMapBuilder getMapBuilder() {
        return this.gameMapBuilder;
    }

    public void backToPushEntry(boolean closeCurrentInventory) {
        if (closeCurrentInventory) {
            this.player.closeInventory(false);
        }
        pushValueInventory.invalidateDataLayout();
        pushValueInventory.open();
    }

    public void backToGroundBlock(boolean closeCurrentInventory) {
        if (closeCurrentInventory) {
            this.player.closeInventory(false);
        }
        this.groundViewInventory.openGroundBlockValueInventory();
    }

    public void openInventory() {
        this.overviewInventory.open();
    }

    public void triggerUpdate() {
        this.overviewInventory.invalidateDataLayout();
    }

    public void triggerGroundViewUpdate() {
        if (this.groundViewInventory != null) {
            this.groundViewInventory.invalidateDataLayout();
            this.groundViewInventory.invalidateGroundValueInventory();
        }
    }

    public void triggerPushViewUpdate(int index) {
        if (this.groundViewInventory != null) {
            this.groundViewInventory.invalidateDataLayout();
            //  this.groundViewInventory.openPushValueInventory(index);
        }
    }

    public void triggerPushValueUpdate(int index) {
        if (this.pushValueInventory != null) {
            this.pushValueInventory.updateLayout(index);
        }
    }

    public void openPushValueInventory() {
        this.pushValueInventory.open();
    }

    /**
     * Opens the {@link GroundViewInventory} for the player which owns the data.
     */
    public void openGroundLayerView() {
        this.groundViewInventory.open();
    }
}
