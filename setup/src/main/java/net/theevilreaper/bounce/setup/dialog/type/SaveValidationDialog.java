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

import java.util.ArrayList;
import java.util.List;

/**
 * Shown when a player tries to save a map which is still missing required data. Offers to either go back and
 * finish it, or to discard the whole setup process.
 */
public final class SaveValidationDialog extends AbstractDialogTemplate<List<String>> {

    public static final Key DIALOG_KEY = Key.key("bounce", "save_validation_dialog");

    public SaveValidationDialog() {
        super(
                Component.text("This map isn't ready yet"),
                Component.text("Delete data"),
                Component.text("Back")
        );
    }

    @Override
    public void open(@NotNull Player player, @Nullable List<String> missingFields) {
        List<DialogBody> body = new ArrayList<>();
        body.add(new DialogBody.PlainMessage(Component.text("The following data is still missing:"), 200));
        body.add(new DialogBody.PlainMessage(Component.empty(), 1));

        if (missingFields == null || missingFields.isEmpty()) {
            body.add(new DialogBody.PlainMessage(Component.text("Unknown", NamedTextColor.RED), 200));
        } else {
            for (String missingField : missingFields) {
                body.add(new DialogBody.PlainMessage(Component.text("- " + missingField, NamedTextColor.RED), 200));
            }
        }

        body.add(new DialogBody.PlainMessage(Component.empty(), 1));
        body.add(new DialogBody.PlainMessage(Component.text("Go back and fill them in, or delete this setup."), 200));

        ShowDialogPacket packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        body,
                        List.of()
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
