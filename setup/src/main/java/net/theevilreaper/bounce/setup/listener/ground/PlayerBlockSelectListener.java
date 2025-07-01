package net.theevilreaper.bounce.setup.listener.ground;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.PlayerBlockSelectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerBlockSelectListener implements Consumer<PlayerBlockSelectEvent> {

    private final Function<UUID, Optional<BounceData>> bounceDataSupplier;

    public PlayerBlockSelectListener(@NotNull Function<UUID, Optional<BounceData>> bounceDataSupplier) {
        this.bounceDataSupplier = bounceDataSupplier;
    }

    @Override
    public void accept(@NotNull PlayerBlockSelectEvent event) {
        Optional<BounceData> data = bounceDataSupplier.apply(event.getPlayer().getUuid());

        if (data.isEmpty()) return;

        BounceData bounceData = data.get();
        Material material = event.getMaterial();
        System.out.println("Player " + event.getPlayer().getUsername() + " selected block: " + material.name());
        Block block = material.block();

        if (event.getPart() == PlayerBlockSelectEvent.GroundPart.BLOCK) {
            this.handleGroundBlockChange(bounceData, block);
            return;
        }
        this.handlePushBlockChange(bounceData, block);
    }

    private void handleGroundBlockChange(@NotNull BounceData bounceData, @NotNull Block block) {
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        mapBuilder.setGroundBlock(block);
        System.out.println("Ground block set to: " + block.name());
        bounceData.triggerGroundViewUpdate();
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToGroundView(true));
    }

    private void handlePushBlockChange(@NotNull BounceData bounceData, @NotNull Block block) {
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        mapBuilder.getPushDataBuilder().add(block, 0);
        bounceData.triggerPushViewUpdate();
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToGroundView(true));
    }
}
