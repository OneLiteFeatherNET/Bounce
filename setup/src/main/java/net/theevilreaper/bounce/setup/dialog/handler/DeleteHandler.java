package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import org.jetbrains.annotations.NotNull;

public final class DeleteHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public DeleteHandler(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        int type = payload.getInt("type");
        OverviewType overviewType = OverviewType.fromOrdinal(type);

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;

            switch (overviewType) {
                case NAME -> data.getMapBuilder().name(null);
                case BUILDER -> data.getMapBuilder().clearBuilders();
                case GAME_SPAWN -> data.getMapBuilder().gameSpawn(null);
                case SPAWN -> data.getMapBuilder().spawn(null);
                default -> {
                    // Nothing to do here
                }
            }

            data.triggerUpdate();
        });
    }
}
