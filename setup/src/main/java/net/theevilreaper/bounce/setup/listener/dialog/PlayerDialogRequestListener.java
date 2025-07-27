package net.theevilreaper.bounce.setup.listener.dialog;

import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.setup.dialog.DialogRegistry;
import net.theevilreaper.bounce.setup.dialog.DialogTemplate;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import net.theevilreaper.bounce.setup.dialog.type.AuthorInputDialog;
import net.theevilreaper.bounce.setup.dialog.type.AuthorRequestDialog;
import net.theevilreaper.bounce.setup.dialog.type.NameInputDialog;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent.*;

public final class PlayerDialogRequestListener implements Consumer<PlayerDialogRequestEvent> {

    private final DialogRegistry dialogRegistry;

    public PlayerDialogRequestListener(@NotNull DialogRegistry dialogRegistry) {
        this.dialogRegistry = dialogRegistry;
    }

    @Override
    public void accept(@NotNull PlayerDialogRequestEvent event) {
        Player player = event.getPlayer();
        Target target = event.getTarget();

        DialogTemplate<?> dialogTemplate;
        switch (target) {
            case Target.SETUP_NAME -> dialogTemplate = dialogRegistry.get(NameInputDialog.DIALOG_KEY);
            case Target.SETUP_AUTHOR -> dialogTemplate = dialogRegistry.get(AuthorInputDialog.DIALOG_KEY);
            case Target.SETUP_REQUEST_AUTHOR -> dialogTemplate = dialogRegistry.get(AuthorRequestDialog.DIALOG_KEY);
            default -> throw new IllegalArgumentException("Unknown target: " + target);
        }

        if (dialogTemplate == null) {
            throw new IllegalStateException("Dialog with key " + dialogTemplate.key() + " not found in registry.");
        }

        dialogTemplate.open(player);
    }
}
