package net.theevilreaper.bounce.setup;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.tag.Tag;
import net.onelitefeather.guira.SetupDataService;
import net.onelitefeather.guira.event.SetupFinishEvent;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.file.GsonFileHandler;
import net.theevilreaper.aves.map.MapProvider;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.common.util.GsonUtil;
import net.theevilreaper.bounce.setup.command.GameModeCommand;
import net.theevilreaper.bounce.setup.command.SetupCommand;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.MapSetupSelectEvent;
import net.theevilreaper.bounce.setup.inventory.InventoryService;
import net.theevilreaper.bounce.setup.listener.PlayerConfigurationListener;
import net.theevilreaper.bounce.setup.listener.PlayerDisconnectListener;
import net.theevilreaper.bounce.setup.listener.PlayerItemListener;
import net.theevilreaper.bounce.setup.listener.PlayerSpawnListener;
import net.theevilreaper.bounce.setup.listener.entity.EntityAddToInstanceListener;
import net.theevilreaper.bounce.setup.listener.map.MapSetupSelectListener;
import net.theevilreaper.bounce.setup.listener.map.SetupFinishListener;
import net.theevilreaper.bounce.setup.map.BounceSetupMapProvider;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.function.Supplier;

public final class BounceSetup implements ListenerHandling {

    public static final Tag<Integer> SETUP_TAG = Tag.Transient("bounce.setup");
    private final MapProvider mapProvider;
    private final SetupDataService<BounceData> setupDataService;
    private final InventoryService inventoryService;
    private final SetupItems setupItems;
    private final FileHandler fileHandler;

    public BounceSetup() {
        Path path = Path.of("");
        this.mapProvider = new BounceSetupMapProvider(path);
        this.setupDataService = SetupDataService.create();
        this.inventoryService = new InventoryService(this.mapProvider::getEntries);
        this.setupItems = new SetupItems();
        this.fileHandler = new GsonFileHandler(GsonUtil.GSON);
        MinecraftServer.getSchedulerManager().buildShutdownTask(this::onShutdown);
    }

    public void initialize() {
        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(node);
        registerListener(node);

        MinecraftServer.getCommandManager().register(new SetupCommand(this.setupDataService));
        MinecraftServer.getCommandManager().register(new GameModeCommand());
    }

    private void onShutdown() {
        this.inventoryService.cleanup();
    }

    private void registerListener(@NotNull EventNode<Event> node) {
        Supplier<Instance> instanceSupplier = this.mapProvider.getActiveInstance();
        node.addListener(AsyncPlayerConfigurationEvent.class, new PlayerConfigurationListener(instanceSupplier));
        node.addListener(PlayerUseItemEvent.class, new PlayerItemListener(this.inventoryService::openMapSetupInventory, this.setupDataService::get));
        node.addListener(PlayerSpawnEvent.class, new PlayerSpawnListener(
                this.setupItems::setOverViewItem,
                player -> this.mapProvider.teleportToSpawn(player, false))
        );
        node.addListener(PlayerDisconnectEvent.class, new PlayerDisconnectListener(this.setupDataService::get));
        node.addListener(MapSetupSelectEvent.class, new MapSetupSelectListener(this.fileHandler, this.setupDataService));

        PlayerConsumer instanceSwitcher = player -> {
            mapProvider.teleportToSpawn(player, true);
            setupItems.setOverViewItem(player);
        };

        node.addListener(
                (Class<SetupFinishEvent<BounceData>>) (Class<?>) SetupFinishEvent.class,
                new SetupFinishListener(instanceSwitcher)
        );

        node.addListener(AddEntityToInstanceEvent.class, new EntityAddToInstanceListener(instanceSupplier, setupItems));

    }
}
