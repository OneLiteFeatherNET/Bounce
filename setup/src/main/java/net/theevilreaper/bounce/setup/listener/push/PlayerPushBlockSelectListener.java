package net.theevilreaper.bounce.setup.listener.push;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.push.PlayerPushBlockSelectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerPushBlockSelectListener implements Consumer<PlayerPushBlockSelectEvent> {

    private final Function<UUID, Optional<BounceData>> bounceDataSupplier;

    public PlayerPushBlockSelectListener(@NotNull Function<UUID, Optional<BounceData>> bounceDataSupplier) {
        this.bounceDataSupplier = bounceDataSupplier;
    }

    @Override
    public void accept(@NotNull PlayerPushBlockSelectEvent event) {
        Optional<BounceData> data = bounceDataSupplier.apply(event.getPlayer().getUuid());

        if (data.isEmpty()) return;

        BounceData bounceData = data.get();
        Material material = event.getMaterial();
        System.out.println("Player " + event.getPlayer().getUsername() + " selected block: " + material.name());
        Block block = material.block();
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        if (hasBlock(mapBuilder.getPushDataBuilder(), block)) {
            System.out.println("Block " + block.name() + " already exists in push data.");
            return;
        }
        mapBuilder.getPushDataBuilder().add(block, 0);
        bounceData.triggerPushViewUpdate();
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToGroundView(true));
    }

    private boolean hasBlock(@NotNull PushData.Builder pushDataBuilder, @NotNull Block block) {
        return pushDataBuilder.getPushValues().stream()
                .anyMatch(pushBlock -> pushBlock.getBlock().equals(block));
    }
}
