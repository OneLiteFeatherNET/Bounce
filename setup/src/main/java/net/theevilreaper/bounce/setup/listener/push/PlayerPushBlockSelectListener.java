package net.theevilreaper.bounce.setup.listener.push;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.push.PlayerPushBlockSelectEvent;
import net.theevilreaper.bounce.setup.util.SetupTags;
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
        Player player = event.getPlayer();

        if (!player.hasTag(SetupTags.PUSH_SLOT_INDEX)) return;

        Optional<BounceData> data = bounceDataSupplier.apply(event.getPlayer().getUuid());

        if (data.isEmpty()) return;
        int blockSelectIndex = player.getTag(SetupTags.PUSH_SLOT_INDEX);

        BounceData bounceData = data.get();
        Material material = event.getMaterial();
        Block block = material.block();
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        if (hasBlock(mapBuilder.getPushDataBuilder(), block)) {
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToPushEntry(true));
            return;
        }
        PushEntry entry = mapBuilder.getPushDataBuilder().getPushValues().get(blockSelectIndex);

        entry.setBlock(block);
        bounceData.triggerPushViewUpdate();
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> bounceData.backToPushEntry(true));
    }

    /**
     * Checks if the given block is already present in the push data builder.
     *
     * @param pushDataBuilder the push data builder to check against
     * @param block           the block to check for
     * @return true if the block is already present, false otherwise
     */
    private boolean hasBlock(@NotNull PushData.Builder pushDataBuilder, @NotNull Block block) {
        return pushDataBuilder.getPushValues().stream()
                .anyMatch(pushBlock -> pushBlock.getBlock().equals(block));
    }
}
