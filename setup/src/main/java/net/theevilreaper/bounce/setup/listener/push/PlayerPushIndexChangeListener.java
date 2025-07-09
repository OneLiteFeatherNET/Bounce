package net.theevilreaper.bounce.setup.listener.push;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.push.PlayerPushIndexChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.theevilreaper.bounce.setup.util.SetupTags.PUSH_SLOT_INDEX;

public final class PlayerPushIndexChangeListener implements Consumer<PlayerPushIndexChangeEvent> {

    private final Function<UUID, Optional<BounceData>> profileGetter;

    public PlayerPushIndexChangeListener(Function<UUID, Optional<BounceData>> profileGetter) {
        this.profileGetter = profileGetter;
    }

    @Override
    public void accept(@NotNull PlayerPushIndexChangeEvent event) {
        Player player = event.getPlayer();
        int index = event.getIndex();

        Optional<BounceData> optionalData = this.profileGetter.apply(player.getUuid());

        if (optionalData.isEmpty()) {
            player.sendMessage("You do not have a setup profile.");
            return;
        }

        BounceData setupData = optionalData.get();

        setupData.triggerPushValueUpdate(index);
        player.closeInventory();
        player.setTag(PUSH_SLOT_INDEX, index);
        MinecraftServer.getSchedulerManager().scheduleNextTick(setupData::openPushValueInventory);
    }
}
