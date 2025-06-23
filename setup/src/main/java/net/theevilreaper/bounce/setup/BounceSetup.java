package net.theevilreaper.bounce.setup;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.onelitefeather.guira.SetupDataService;
import net.theevilreaper.aves.map.MapProvider;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.listener.PlayerConfigurationListener;
import net.theevilreaper.bounce.setup.map.BounceSetupMapProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class BounceSetup implements ListenerHandling {

    private final MapProvider mapProvider;
    private final SetupDataService<BounceData> setupDataService;

    public BounceSetup() {
        Path path = Path.of("");
        this.mapProvider = new BounceSetupMapProvider(path);
        this.setupDataService = SetupDataService.create();
    }

    public void initialize() {
        GlobalEventHandler node = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(node);
        registerListener(node);
    }

    private void registerListener(@NotNull EventNode<Event> node) {
        node.addListener(AsyncPlayerConfigurationEvent.class, new PlayerConfigurationListener(this.mapProvider.getActiveInstance()));
    }
}
