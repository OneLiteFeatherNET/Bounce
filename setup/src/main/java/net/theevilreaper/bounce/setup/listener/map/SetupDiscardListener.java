package net.theevilreaper.bounce.setup.listener.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.onelitefeather.guira.data.SetupData;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.event.map.SetupDiscardEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SetupDiscardListener implements Consumer<SetupDiscardEvent> {

    private final PlayerConsumer instanceSwitcher;

    public SetupDiscardListener(@NotNull PlayerConsumer instanceSwitcher) {
        this.instanceSwitcher = instanceSwitcher;
    }

    @Override
    public void accept(@NotNull SetupDiscardEvent event) {
        SetupData setupData = event.getData();

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(setupData.getId());
        this.instanceSwitcher.accept(player);
        setupData.reset();
    }
}
