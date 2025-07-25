package net.theevilreaper.bounce.setup.listener.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.dialog.*;
import net.theevilreaper.bounce.setup.dialog.type.AuthorInputDialog;
import net.theevilreaper.bounce.setup.dialog.type.AuthorRequestDialog;
import net.theevilreaper.bounce.setup.dialog.type.NameInputDialog;
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

        DialogTemplate dialogTemplate = dialogRegistry.get(key);

        if (dialogTemplate == null) return;


        CompoundBinaryTag dialogData = (CompoundBinaryTag) payload;

        if (dialogTemplate instanceof AuthorRequestDialog) {
            int amount = dialogData.getInt("amount", 1);
            String text = amount == 1 ? "Setup author" : "Setup authors";
            player.setTag(SetupTags.AUTHOR_AMOUNT_TAG, amount);
            dialogRegistry.get(AuthorInputDialog.DIALOG_KEY).open(player);
        }

        setupDataGetter.get(player.getUuid()).ifPresent(setupData -> {
            BounceData data = (BounceData) setupData;
            if (dialogTemplate instanceof NameInputDialog) {
                String name = dialogData.getString("name");
                data.getMapBuilder().name(name);
            }
        });

    }
}
