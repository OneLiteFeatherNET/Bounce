package net.theevilreaper.bounce.setup.listener.dialog;

import net.theevilreaper.bounce.setup.dialog.DialogRegistry;
import net.theevilreaper.bounce.setup.dialog.DialogTemplate;
import net.theevilreaper.bounce.setup.dialog.type.DeleteDialog;
import net.theevilreaper.bounce.setup.event.map.PlayerDeletePromptEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PlayerDeletePromptListener implements Consumer<PlayerDeletePromptEvent> {

    private final DialogRegistry dialogRegistry;

    public PlayerDeletePromptListener(@NotNull DialogRegistry dialogRegistry) {
        this.dialogRegistry = dialogRegistry;
    }

    @Override
    public void accept(@NotNull PlayerDeletePromptEvent event) {
        DialogTemplate<?> dialog = dialogRegistry.get(DeleteDialog.DIALOG_KEY);

        if (dialog == null) {
            throw new IllegalStateException("Dialog with key " + DeleteDialog.DIALOG_KEY + " not found in registry.");
        }

        switch (dialog) {
            case DeleteDialog deleteDialog -> deleteDialog.open(event.getPlayer(), event.getType());
            default -> throw new IllegalStateException("Unexpected dialog type: " + dialog.getClass().getCanonicalName());
        }
    }
}
