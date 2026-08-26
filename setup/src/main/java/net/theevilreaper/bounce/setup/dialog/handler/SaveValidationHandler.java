package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.map.SetupDiscardEvent;
import org.jetbrains.annotations.NotNull;

public final class SaveValidationHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public SaveValidationHandler(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData ->
                EventDispatcher.call(new SetupDiscardEvent((BounceData) setupData)));
    }
}
