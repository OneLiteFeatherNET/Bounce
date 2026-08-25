package net.theevilreaper.bounce.setup.listener.dialog;

import net.theevilreaper.bounce.setup.dialog.MapDialogs;
import net.theevilreaper.bounce.setup.event.map.SaveValidationPromptEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class SaveValidationPromptListener implements Consumer<SaveValidationPromptEvent> {

    @Override
    public void accept(@NotNull SaveValidationPromptEvent event) {
        MapDialogs.openSaveValidationDialog(event.getPlayer(), event.getMissingFields());
    }
}
