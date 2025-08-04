package net.theevilreaper.bounce.setup.listener.ground;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.ground.PlayerGroundBlockSelectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class PlayerBlockSelectListener implements Consumer<PlayerGroundBlockSelectEvent> {

    private final OptionalSetupDataGetter bounceDataSupplier;

    public PlayerBlockSelectListener(@NotNull OptionalSetupDataGetter bounceDataSupplier) {
        this.bounceDataSupplier = bounceDataSupplier;
    }

    @Override
    public void accept(@NotNull PlayerGroundBlockSelectEvent event) {
        Optional<SetupData> data = bounceDataSupplier.get(event.getPlayer().getUuid());

        if (data.isEmpty()) return;

        BounceData bounceData = ((BounceData) data.get());
        Material material = event.getMaterial();
        Block block = material.block();
        this.handleGroundBlockChange(bounceData, block);
    }

    private void handleGroundBlockChange(@NotNull BounceData bounceData, @NotNull Block block) {
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        mapBuilder.groundBlock(block);
        bounceData.triggerGroundViewUpdate();
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToGroundBlock(true));
    }
}
