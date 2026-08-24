package net.theevilreaper.bounce.setup.data;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.world.DimensionType;
import net.onelitefeather.falco.anvil.FalcoAnvilLoader;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.event.SetupFinishEvent;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.util.GsonUtil;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.inventory.ground.AreaViewInventory;
import net.theevilreaper.bounce.setup.inventory.ground.GroundViewInventory;
import net.theevilreaper.bounce.setup.inventory.overview.MapOverviewInventory;
import net.theevilreaper.bounce.setup.inventory.push.PushValueInventory;
import net.theevilreaper.bounce.setup.util.SetupTags;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

public final class BounceData implements SetupData {

    private static final Pos SPAWN_POINT = new Pos(0, 100, 0);
    private final UUID owner;
    private final MapEntry mapEntry;
    private final Player player;

    private InstanceContainer instance;
    private FalcoAnvilLoader loader;
    private GameMapBuilder gameMapBuilder;
    private MapOverviewInventory overviewInventory;
    private GroundViewInventory groundViewInventory;
    private PushValueInventory pushValueInventory;
    private AreaViewInventory areaViewInventory;

    public BounceData(UUID owner, MapEntry mapEntry) {
        this.owner = owner;
        this.mapEntry = mapEntry;
        Player foundPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(owner);

        if (foundPlayer == null) {
            throw new IllegalArgumentException("Player with UUID " + owner + " is not online.");
        }

        this.player = foundPlayer;
        this.loadData();
    }

    public void teleport(Player player) {
        Pos spawnPoint = this.gameMapBuilder.getSpawnOrDefault(SPAWN_POINT);
        player.setInstance(this.instance, spawnPoint);
    }

    @Override
    public void save() {
        if (mapEntry.getMapFile() == null || !Files.exists(mapEntry.getMapFile())) {
            this.mapEntry.createFile();
        }
        GameMap map = this.gameMapBuilder.build();
        GsonUtil.GSON_FILE_HANDLER.save(mapEntry.getMapFile(), map);
        EventDispatcher.call(new SetupFinishEvent(this));
    }

    @Override
    public void reset() {
        player.removeTag(SetupTags.SETUP_TAG);
        player.removeTag(SetupTags.PUSH_SLOT_INDEX);
        this.overviewInventory.unregister();
        this.groundViewInventory.unregister();
        this.pushValueInventory.unregister();

        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
           MinecraftServer.getInstanceManager().unregisterInstance(this.instance);
            try {
                this.loader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void loadData() {
        if (this.mapEntry.getMapFile() == null) {
            this.gameMapBuilder = new GameMapBuilder();
        } else {
            Optional<GameMap> mapData = GsonUtil.GSON_FILE_HANDLER.load(mapEntry.getMapFile(), GameMap.class);
            // Initialize with a new BaseMap if loading fails
            mapData.ifPresentOrElse(gameMap ->
                    this.gameMapBuilder = new GameMapBuilder(gameMap),
                    () -> this.gameMapBuilder = new GameMapBuilder()
            );
        }

        this.groundViewInventory = new GroundViewInventory(this.player, this.gameMapBuilder);
        this.groundViewInventory.register();

        this.overviewInventory = new MapOverviewInventory(this.player, this.gameMapBuilder);
        this.overviewInventory.register();

        this.pushValueInventory = new PushValueInventory(this.player, this.gameMapBuilder);
        this.pushValueInventory.register();

        this.areaViewInventory = new AreaViewInventory(this.player, this.gameMapBuilder);
        this.areaViewInventory.register();

        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        this.loader = new FalcoAnvilLoader(this.mapEntry.getDirectoryRoot(), DimensionType.OVERWORLD.key());
        this.instance.setChunkLoader(this.loader);

        MinecraftServer.getInstanceManager().registerInstance(this.instance);
    }

    public GameMapBuilder getMapBuilder() {
        return this.gameMapBuilder;
    }

    public void backToPushEntry(boolean closeCurrentInventory) {
        if (closeCurrentInventory) {
            this.player.closeInventory();
        }
        pushValueInventory.invalidateDataLayout();
        pushValueInventory.open();
    }

    public void backToGroundBlock(boolean closeCurrentInventory) {
        if (closeCurrentInventory) {
            this.player.closeInventory();
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
        this.groundViewInventory.invalidateDataLayout();
        this.groundViewInventory.invalidateGroundValueInventory();
    }

    public void triggerPushViewUpdate() {
        this.groundViewInventory.invalidateDataLayout();
    }

    public void triggerPushValueUpdate(int index) {
        this.pushValueInventory.updateLayout(index);
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

    public void openGroundBlockView() {
        this.groundViewInventory.openGroundBlockValueInventory();
    }

    /**
     * Opens the {@link AreaViewInventory} for the player which owns the data.
     */
    public void openAreaView() {
        this.areaViewInventory.open();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId() {
        return this.owner;
    }
}
