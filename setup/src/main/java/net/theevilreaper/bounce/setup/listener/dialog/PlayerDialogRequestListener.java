package net.theevilreaper.bounce.setup.listener.dialog;

import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.setup.dialog.AuthorDialogs;
import net.theevilreaper.bounce.setup.dialog.MapDialogs;
import net.theevilreaper.bounce.setup.dialog.ValueDialogs;
import net.theevilreaper.bounce.setup.dialog.event.PlayerDialogRequestEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PlayerDialogRequestListener implements Consumer<PlayerDialogRequestEvent> {

    @Override
    public void accept(@NotNull PlayerDialogRequestEvent event) {
        Player player = event.getPlayer();

        switch (event.getTarget()) {
            case SETUP_NAME -> MapDialogs.openNameDialog(player);
            case SETUP_REQUEST_AUTHOR -> AuthorDialogs.openAuthorAmountDialog(player);
            case SETUP_BLOCK_BOUNCE -> ValueDialogs.openBounceValue(player);
            case SETUP_BLOCK_WEIGHT -> ValueDialogs.openWeight(player);
            case SETUP_SHUFFLE_INTERVAL -> ValueDialogs.openShuffleInterval(player);
            case SETUP_RESHUFFLE_PERCENTAGE -> ValueDialogs.openReshufflePercentage(player);
        }
    }
}
