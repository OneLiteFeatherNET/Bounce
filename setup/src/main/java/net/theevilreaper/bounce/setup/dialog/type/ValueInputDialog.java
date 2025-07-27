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

public final class ValueInputDialog extends AbstractDialogTemplate<Integer> {

    public static final Key DIALOG_KEY = Key.key("bounce", "value_setup_dialog");

    public ValueInputDialog() {
        super(
                Component.text("Change block boost"),
                Component.text("Click to confirm"),
                Component.text("Click to cancel")
        );
    }

    @Override
    public void open(@NotNull Player player) {
        this.open(player, 1);
    }

    @Override
    public void open(@NotNull Player player, @Nullable Integer data) {
        ShowDialogPacket packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("How much the block should bounce?"), 200)
                        ),
                        List.of(
                                new DialogInput.NumberRange("bounce_amount", 200, Component.text("Amount"), "options.generic_value", 1, 10, 1f, 1f)
                        )
                ),
                new DialogActionButton(
                        submitComponent,
                        Component.text("Click to confirm", NamedTextColor.GREEN),
                        100,
                        new DialogAction.DynamicCustom(DIALOG_KEY, CompoundBinaryTag.builder().build())
                ),
                new DialogActionButton(
                        cancelComponent,
                        Component.text("Click to cancel", NamedTextColor.RED),
                        101,
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
