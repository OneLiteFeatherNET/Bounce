package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

public final class ReshufflePercentageHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public ReshufflePercentageHandler(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        float percentage = payload.getFloat("reshuffle_percentage", 10.0f);
        double reshufflePercentage = Math.max(0.0, Math.min(1.0, percentage / 100.0));

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            data.getMapBuilder().reshufflePercentage(reshufflePercentage);
            data.triggerAreaViewUpdate();
        });
    }
}
