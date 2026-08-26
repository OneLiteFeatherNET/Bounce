package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;
import net.theevilreaper.bounce.setup.dialog.AuthorDialogs;

public final class AuthorAmountHandler implements DialogHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload) {
        int amount = payload.getInt("amount", 1);
        if (amount <= 0) return;

        AuthorDialogs.openAuthorInputDialog(event.getPlayer(), amount);
    }
}
