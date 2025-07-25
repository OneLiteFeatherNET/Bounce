package net.theevilreaper.bounce.setup.dialog.type;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.dialog.*;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.ShowDialogPacket;
import net.theevilreaper.bounce.setup.dialog.DialogTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuthorRequestDialog implements DialogTemplate {

    private static final Key DIALOG_KEY = Key.key("bounce", "author_amount_dialog");

    private final Component header;
    private final Component submitComponent;
    private final Component cancelComponent;

    public AuthorRequestDialog(@NotNull Component header, @NotNull Component submitComponent, @NotNull Component cancelComponent) {
        this.header = header;
        this.submitComponent = submitComponent;
        this.cancelComponent = cancelComponent;
    }

    @Override
    public void open(@NotNull Player player) {
        var packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("aa"), 10)
                        ),
                        List.of(
                                new DialogInput.NumberRange("amount", 200, Component.text("Amount"), "", 1, 10, 1f, 1f)
                        )
                ),
                new DialogActionButton(
                        submitComponent,
                        Component.text("§7Click to submit"),
                        100,
                        new DialogAction.DynamicCustom(DIALOG_KEY, CompoundBinaryTag.builder().build())
                ),
                new DialogActionButton(
                        cancelComponent,
                        Component.text("§7Click to cancel"),
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
