package net.theevilreaper.bounce.setup.dialog.type;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.dialog.*;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.common.ShowDialogPacket;
import net.theevilreaper.bounce.setup.dialog.DialogTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class NameInputDialog implements DialogTemplate<String> {

    public static final Key DIALOG_KEY = Key.key("bounce", "name_setup_dialog");

    private final Component header;
    private final Component submitComponent;
    private final Component cancelComponent;

    public NameInputDialog(@NotNull Component header, @NotNull Component submitComponent, @NotNull Component cancelComponent) {
        this.header = header;
        this.submitComponent = submitComponent;
        this.cancelComponent = cancelComponent;
    }

    /**
     * Opens the name input dialog for the given player with the provided data.
     * @param player the player to open the dialog for
     * @param data the initial data to pre-fill the input field, can be null
     */
    public void open(@NotNull Player player, @Nullable String  data) {
        String initialName = data == null ? "" : data;
        var packet = new ShowDialogPacket(new Dialog.Confirmation(
                new DialogMetadata(
                        header,
                        null,
                        false,
                        false,
                        DialogAfterAction.CLOSE,
                        List.of(
                                new DialogBody.PlainMessage(Component.text("How the name of the map should be?"), 200)
                        ),
                        List.of(
                                new DialogInput.Text("name", 200, Component.text("Name"), true, initialName, 32, null)
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
