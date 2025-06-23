package net.theevilreaper.bounce.setup;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.aves.map.MapProvider;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.inventory.MapSetupInventory;
import net.theevilreaper.bounce.setup.listener.PlayerConfigurationListener;
import net.theevilreaper.bounce.setup.listener.PlayerItemListener;
import net.theevilreaper.bounce.setup.listener.PlayerSpawnListener;
import net.theevilreaper.bounce.setup.map.BounceSetupMapProvider;
import net.theevilreaper.bounce.setup.util.SetupItems;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class BounceSetup implements ListenerHandling {

    private final MapProvider mapProvider;
    private final SetupDataService<BounceData> setupDataService;
    private final MapSetupInventory mapSetupInventory;
    private final SetupItems setupItems;

    public BounceSetup() {
        Path path = Path.of("");
        this.mapProvider = new BounceSetupMapProvider(path);
        this.setupDataService = SetupDataService.create();
        this.mapSetupInventory = new MapSetupInventory(this.mapProvider::getEntries);
        this.setupItems = new SetupItems();

        MinecraftServer.getSchedulerManager().buildShutdownTask(this::onShutdown);
    }

    public void initialize() {
        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(node);
        registerListener(node);

        this.mapSetupInventory.register();
    }

    private void onShutdown() {
        this.mapSetupInventory.unregister();
    }

    private void registerListener(@NotNull EventNode<Event> node) {
        node.addListener(AsyncPlayerConfigurationEvent.class, new PlayerConfigurationListener(this.mapProvider.getActiveInstance()));
        node.addListener(PlayerUseItemEvent.class, new PlayerItemListener(this::openMapSetupInventory, this.setupDataService::get));
        node.addListener(PlayerSpawnEvent.class, new PlayerSpawnListener(
                this.setupItems::setOverViewItem,
                player -> this.mapProvider.teleportToSpawn(player, false))
        );
    }

    private void openMapSetupInventory(@NotNull Player player) {
        player.openInventory(this.mapSetupInventory.getInventory());
    }
}
