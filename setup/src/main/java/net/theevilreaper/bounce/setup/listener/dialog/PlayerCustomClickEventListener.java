package net.theevilreaper.bounce.setup.listener.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.dialog.*;
import net.theevilreaper.bounce.setup.dialog.type.AuthorInputDialog;
import net.theevilreaper.bounce.setup.dialog.type.AuthorRequestDialog;
import net.theevilreaper.bounce.setup.dialog.type.DeleteDialog;
import net.theevilreaper.bounce.setup.dialog.type.NameInputDialog;
import net.theevilreaper.bounce.setup.inventory.overview.OverviewType;
import net.theevilreaper.bounce.setup.util.SetupTags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PlayerCustomClickEventListener implements Consumer<PlayerCustomClickEvent> {

    private final DialogRegistry dialogRegistry;
    private final OptionalSetupDataGetter setupDataGetter;

    public PlayerCustomClickEventListener(
            @NotNull DialogRegistry dialogRegistry,
            @NotNull OptionalSetupDataGetter setupDataGetter
    ) {
        this.dialogRegistry = dialogRegistry;
        this.setupDataGetter = setupDataGetter;
    }

    @Override
    public void accept(@NotNull PlayerCustomClickEvent event) {
        Player player = event.getPlayer();

        if (!player.hasTag(SetupTags.SETUP_TAG)) return;

        Key key = event.getKey();
        BinaryTag payload = event.getPayload();

        if (payload == null) return;

        DialogTemplate<?> dialogTemplate = dialogRegistry.get(key);

        if (dialogTemplate == null) return;

        CompoundBinaryTag dialogData = (CompoundBinaryTag) payload;

        if (dialogTemplate instanceof AuthorRequestDialog) {
            int amount = dialogData.getInt("amount", 1);
            player.setTag(SetupTags.AUTHOR_AMOUNT_TAG, amount);
            dialogRegistry.get(AuthorInputDialog.DIALOG_KEY).open(player);
            return;
        }

        setupDataGetter.get(player.getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;

            switch (dialogTemplate) {
                case NameInputDialog ignored -> this.handleNameSet(data, dialogData);
                case AuthorInputDialog ignored -> handleAuthorSet(data, dialogData);
                case DeleteDialog ignored -> this.handleDataDelete(data, dialogData);
                default ->
                        throw new IllegalStateException("Unexpected dialog type: " + dialogTemplate.getClass().getCanonicalName());
            }
        });
    }

    /**
     * Handles the setting of authors based on the dialog data provided.
     *
     * @param data       the BounceData instance containing the map builder
     * @param dialogData the dialog data containing the authors to set
     */
    private void handleNameSet(@NotNull BounceData data, @NotNull CompoundBinaryTag dialogData) {
        String name = dialogData.getString("name");
        if (name.trim().isEmpty()) return;
        data.getMapBuilder().name(name);
        data.triggerUpdate();
    }

    /**
     * Handles the setting of authors based on the dialog data provided.
     *
     * @param data       the BounceData instance containing the map builder
     * @param dialogData the dialog data containing the authors to set
     */
    private void handleAuthorSet(@NotNull BounceData data, @NotNull CompoundBinaryTag dialogData) {
        int amount = dialogData.getInt("amount", 1);

        for (int i = 0; i < amount; i++) {
            String author = dialogData.getString("author" + i);
            if (author.trim().isEmpty()) continue;
            data.getMapBuilder().builder(author);
        }
        data.triggerUpdate();
    }

    /**
     * Handles the deletion of data based on the dialog data provided.
     *
     * @param data       the BounceData instance containing the map builder
     * @param dialogData the dialog data containing the type of data to delete
     */
    private void handleDataDelete(@NotNull BounceData data, @NotNull CompoundBinaryTag dialogData) {
        int type = dialogData.getInt("type");
        OverviewType mappedType = OverviewType.fromOrdinal(type);

        switch (mappedType) {
            case NAME -> data.getMapBuilder().name(null);
            case BUILDER -> data.getMapBuilder().clearBuilders();
            case GAME_SPAWN -> data.getMapBuilder().gameSpawn(null);
            case SPAWN -> data.getMapBuilder().spawn(null);
        }

        data.triggerUpdate();
    }
}
