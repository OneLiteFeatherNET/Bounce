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
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

public final class BounceData extends BaseSetupData<GameMap> {

    private static final Pos SPAWN_POINT = new Pos(0, 100, 0);
    private final FileHandler fileHandler;

    private InstanceContainer instance;
    private Player player;


    public BounceData(@NotNull UUID owner, @NotNull MapEntry mapEntry, @NotNull FileHandler fileHandler) {
        super(owner, mapEntry);
        this.fileHandler = fileHandler;this.loadData();
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);

        if (player == null) {
            throw new IllegalArgumentException("Player with UUID " + uuid + " is not online.");
        }

        //this.viewInventory = new LobbyViewInventory(this.map);
    }

    public void openInventory() {
       // player.openInventory(this.viewInventory.getInventory());
    }

    public void triggerUpdate() {
      //  this.viewInventory.invalidateDataLayout();
    }

    public void teleport(@NotNull Player player) {
        Pos spawnPoint = map.getSpawnOrDefault(SPAWN_POINT);
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

    }

    @Override
    public void loadData() {
        if (this.mapEntry != null) return;
        Optional<GameMap> mapData = fileHandler.load(mapEntry.getMapFile(), GameMap.class);
        // Initialize with a new BaseMap if loading fails
        this.map = mapData.orElseGet(GameMap::new);

        this.instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        AnvilLoader anvilLoader = new AnvilLoader(this.mapEntry.getDirectoryRoot());
        this.instance.setChunkLoader(anvilLoader);
        MinecraftServer.getInstanceManager().registerInstance(this.instance);
    }
}
