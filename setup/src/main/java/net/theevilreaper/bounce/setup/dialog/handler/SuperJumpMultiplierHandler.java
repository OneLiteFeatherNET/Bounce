package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.setup.data.BounceData;

public final class SuperJumpMultiplierHandler implements DialogHandler {

    private static final double MIN_MULTIPLIER = 1.0;
    private static final double MAX_MULTIPLIER = 3.0;

    private final OptionalSetupDataGetter setupDataGetter;

    public SuperJumpMultiplierHandler(OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        float multiplier = payload.getFloat("super_jump_multiplier", (float) GameMap.DEFAULT_SUPER_JUMP_MULTIPLIER);
        double superJumpMultiplier = Math.clamp(multiplier, MIN_MULTIPLIER, MAX_MULTIPLIER);

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            data.getMapBuilder().superJumpMultiplier(superJumpMultiplier);
            data.triggerAreaViewUpdate();
        });
    }
}
