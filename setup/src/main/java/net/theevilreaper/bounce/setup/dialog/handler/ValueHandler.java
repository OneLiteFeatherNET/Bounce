package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.util.SetupTags;

public final class ValueHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public ValueHandler(OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        Player player = event.getPlayer();
        int amount = payload.getInt("bounce_amount", 0);
        int valueIndex = player.hasTag(SetupTags.PUSH_SLOT_INDEX) ? player.getTag(SetupTags.PUSH_SLOT_INDEX) : 0;
        player.removeTag(SetupTags.PUSH_SLOT_INDEX);

        setupDataGetter.get(player.getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            data.getMapBuilder().getPushDataBuilder().getPushValues().get(valueIndex).setValue(amount);
            if (valueIndex == 0) {
                data.triggerGroundViewUpdate();
            } else {
                data.triggerPushValueUpdate(valueIndex);
            }
        });
    }
}
