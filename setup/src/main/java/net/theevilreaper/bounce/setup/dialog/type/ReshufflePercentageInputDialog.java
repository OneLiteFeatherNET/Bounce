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

public final class ReshufflePercentageInputDialog extends AbstractDialogTemplate<Float> {

    public static final Key DIALOG_KEY = Key.key("bounce", "reshuffle_percentage_setup_dialog");

    public ReshufflePercentageInputDialog() {
        super(
                Component.text("Change reshuffle percentage"),
                Component.text("Click to confirm"),
                Component.text("Click to cancel")
        );
    }

    @Override
    public void open(@NotNull Player player) {
        this.open(player, 10.0f);
    }

    @Override
    public void open(@NotNull Player player, @Nullable Float data) {
        float initial = data != null ? data : 10.0f;
        ShowDialogPacket packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("Percentage of the area re-rolled\non each reshuffle (0 - 100%):"), 320)
                        ),
                        List.of(
                                new DialogInput.NumberRange("reshuffle_percentage", 320, Component.text("Percentage"), "options.percent_value", 0f, 100f, initial, 0.1f)
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
