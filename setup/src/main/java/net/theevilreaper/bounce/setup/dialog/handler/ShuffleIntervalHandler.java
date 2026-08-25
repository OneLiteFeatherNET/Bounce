package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

public final class ShuffleIntervalHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public ShuffleIntervalHandler(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        int ticks = (int) payload.getFloat("interval_ticks", 100f);
        if (ticks < 20) ticks = 20;
        int finalTicks = ticks;

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            data.getMapBuilder().shuffleIntervalTicks(finalTicks);
            data.triggerAreaViewUpdate();
        });
    }
}
