package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.setup.data.BounceData;

public final class PowerUpHeightHandler implements DialogHandler {

    private static final double MIN_HEIGHT = 0.5;
    private static final double MAX_HEIGHT = 5.0;

    private final OptionalSetupDataGetter setupDataGetter;

    public PowerUpHeightHandler(OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        float height = payload.getFloat("power_up_height", (float) GameMap.DEFAULT_POWER_UP_HEIGHT);
        double powerUpHeight = Math.clamp(height, MIN_HEIGHT, MAX_HEIGHT);

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            data.getMapBuilder().powerUpHeight(powerUpHeight);
            data.triggerAreaViewUpdate();
        });
    }
}
