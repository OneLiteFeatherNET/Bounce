package net.theevilreaper.bounce.setup.listener.dialog;

import net.theevilreaper.bounce.setup.dialog.DialogRegistry;
import net.theevilreaper.bounce.setup.dialog.DialogTemplate;
import net.theevilreaper.bounce.setup.dialog.type.SaveValidationDialog;
import net.theevilreaper.bounce.setup.event.map.SaveValidationPromptEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SaveValidationPromptListener implements Consumer<SaveValidationPromptEvent> {

    private final DialogRegistry dialogRegistry;

    public SaveValidationPromptListener(@NotNull DialogRegistry dialogRegistry) {
        this.dialogRegistry = dialogRegistry;
    }

    @Override
    public void accept(@NotNull SaveValidationPromptEvent event) {
        DialogTemplate<?> dialog = dialogRegistry.get(SaveValidationDialog.DIALOG_KEY);

        if (dialog == null) {
            throw new IllegalStateException("Dialog with key " + SaveValidationDialog.DIALOG_KEY + " not found in registry.");
        }

        switch (dialog) {
            case SaveValidationDialog saveValidationDialog -> saveValidationDialog.open(event.getPlayer(), event.getMissingFields());
            default -> throw new IllegalStateException("Unexpected dialog type: " + dialog.getClass().getCanonicalName());
        }
    }
}
