package net.theevilreaper.bounce.setup.listener.map;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.onelitefeather.guira.event.SetupFinishEvent;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SetupFinishListener implements Consumer<SetupFinishEvent<BounceData>> {

    private final PlayerConsumer instanceSwitcher;

    public SetupFinishListener(@NotNull PlayerConsumer instanceSwitcher) {
        this.instanceSwitcher = instanceSwitcher;
    }

    @Override
    public void accept(@NotNull SetupFinishEvent<BounceData> event) {
        BounceData setupData = event.getData();

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(setupData.getId());
        this.instanceSwitcher.accept(player);
        setupData.reset();
    }
}
