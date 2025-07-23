package net.theevilreaper.bounce.setup.listener.push;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.push.PlayerPushBlockSelectEvent;
import net.theevilreaper.bounce.setup.util.SetupTags;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public class PlayerPushBlockSelectListener implements Consumer<PlayerPushBlockSelectEvent> {

    private final OptionalSetupDataGetter bounceDataSupplier;

    public PlayerPushBlockSelectListener(@NotNull OptionalSetupDataGetter bounceDataSupplier) {
        this.bounceDataSupplier = bounceDataSupplier;
    }

    @Override
    public void accept(@NotNull PlayerPushBlockSelectEvent event) {
        Player player = event.getPlayer();

        if (!player.hasTag(SetupTags.PUSH_SLOT_INDEX)) return;

        Optional<SetupData> data = bounceDataSupplier.get(event.getPlayer().getUuid());

        if (data.isEmpty()) return;
        int blockSelectIndex = player.getTag(SetupTags.PUSH_SLOT_INDEX);

        BounceData bounceData = ((BounceData) data.get());
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
