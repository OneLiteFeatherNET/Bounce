package net.theevilreaper.bounce.setup.listener.dialog;

import net.theevilreaper.bounce.setup.dialog.MapDialogs;
import net.theevilreaper.bounce.setup.event.map.PlayerDeletePromptEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PlayerDeletePromptListener implements Consumer<PlayerDeletePromptEvent> {

    @Override
    public void accept(@NotNull PlayerDeletePromptEvent event) {
        MapDialogs.openDeleteDialog(event.getPlayer(), event.getType());
    }
}
