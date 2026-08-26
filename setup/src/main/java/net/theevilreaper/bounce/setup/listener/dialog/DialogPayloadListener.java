package net.theevilreaper.bounce.setup.listener.dialog;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.setup.dialog.AuthorDialogs;
import net.theevilreaper.bounce.setup.dialog.MapDialogs;
import net.theevilreaper.bounce.setup.dialog.ValueDialogs;
import net.theevilreaper.bounce.setup.dialog.handler.AuthorAmountHandler;
import net.theevilreaper.bounce.setup.dialog.handler.AuthorInputHandler;
import net.theevilreaper.bounce.setup.dialog.handler.DeleteHandler;
import net.theevilreaper.bounce.setup.dialog.handler.DialogHandler;
import net.theevilreaper.bounce.setup.dialog.handler.NameHandler;
import net.theevilreaper.bounce.setup.dialog.handler.ReshufflePercentageHandler;
import net.theevilreaper.bounce.setup.dialog.handler.SaveValidationHandler;
import net.theevilreaper.bounce.setup.dialog.handler.ShuffleIntervalHandler;
import net.theevilreaper.bounce.setup.dialog.handler.ValueHandler;
import net.theevilreaper.bounce.setup.dialog.handler.WeightHandler;
import net.theevilreaper.bounce.setup.util.SetupTags;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public final class DialogPayloadListener implements Consumer<PlayerCustomClickEvent> {

    private final Map<Key, DialogHandler> handlers;

    public DialogPayloadListener(@NotNull OptionalSetupDataGetter setupDataGetter) {
        this.handlers = Map.ofEntries(
                Map.entry(MapDialogs.NAME_KEY, new NameHandler(setupDataGetter)),
                Map.entry(MapDialogs.DELETE_KEY, new DeleteHandler(setupDataGetter)),
                Map.entry(MapDialogs.SAVE_VALIDATION_KEY, new SaveValidationHandler(setupDataGetter)),
                Map.entry(AuthorDialogs.AUTHOR_AMOUNT_KEY, new AuthorAmountHandler()),
                Map.entry(AuthorDialogs.AUTHOR_INPUT_KEY, new AuthorInputHandler(setupDataGetter)),
                Map.entry(ValueDialogs.VALUE_KEY, new ValueHandler(setupDataGetter)),
                Map.entry(ValueDialogs.WEIGHT_KEY, new WeightHandler(setupDataGetter)),
                Map.entry(ValueDialogs.SHUFFLE_INTERVAL_KEY, new ShuffleIntervalHandler(setupDataGetter)),
                Map.entry(ValueDialogs.RESHUFFLE_PERCENTAGE_KEY, new ReshufflePercentageHandler(setupDataGetter))
        );
    }

    @Override
    public void accept(@NotNull PlayerCustomClickEvent event) {
        Player player = event.getPlayer();
        if (!player.hasTag(SetupTags.SETUP_TAG)) return;

        Key key = event.getKey();
        BinaryTag payload = event.getPayload();
        if (payload == null) return;

        DialogHandler handler = this.handlers.get(key);
        if (handler == null) return;

        handler.handle(event, (CompoundBinaryTag) payload);
    }
}
