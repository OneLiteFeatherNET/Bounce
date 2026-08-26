package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

public final class AuthorInputHandler implements DialogHandler {

    private final OptionalSetupDataGetter setupDataGetter;

    public AuthorInputHandler(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.setupDataGetter = setupDataGetter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        int amount = payload.getInt("amount", 1);

        setupDataGetter.get(event.getPlayer().getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;

            for (int i = 0; i < amount; i++) {
                String author = payload.getString("author" + i);
                if (author.trim().isEmpty()) continue;
                data.getMapBuilder().builder(author);
            }
            data.triggerUpdate();
        });
    }
}
