package net.theevilreaper.bounce.setup.dialog.type;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.dialog.*;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.ShowDialogPacket;
import net.theevilreaper.bounce.setup.dialog.AbstractDialogTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ShuffleIntervalInputDialog extends AbstractDialogTemplate<Float> {

    public static final Key DIALOG_KEY = Key.key("bounce", "shuffle_interval_setup_dialog");

    public ShuffleIntervalInputDialog() {
        super(
                Component.text("Change shuffle interval"),
                Component.text("Click to confirm"),
                Component.text("Click to cancel")
        );
    }

    @Override
    public void open(@NotNull Player player) {
        this.open(player, 100.0f);
    }

    @Override
    public void open(@NotNull Player player, @Nullable Float data) {
        float initial = data != null ? data : 100.0f;
        ShowDialogPacket packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("Reshuffle interval in ticks (20 ticks = 1 second):"), 320)
                        ),
                        List.of(
                                new DialogInput.NumberRange("interval_ticks", 320, Component.text("Interval"), "options.generic_value", 20f, 600f, initial, 10f)
                        )
                ),
                new DialogActionButton(
                        submitComponent,
                        Component.text("Click to confirm", NamedTextColor.GREEN),
                        155,
                        new DialogAction.DynamicCustom(DIALOG_KEY, CompoundBinaryTag.builder().build())
                ),
                new DialogActionButton(
                        cancelComponent,
                        Component.text("Click to cancel", NamedTextColor.RED),
                        155,
                        null
                )
        ));
        player.sendPacket(packet);
    }

    @Override
    public @NotNull Key key() {
        return DIALOG_KEY;
    }
}
