package net.theevilreaper.bounce.setup.dialog.handler;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.event.player.PlayerCustomClickEvent;

/**
 * Represents a handler which reacts to the payload of a single dialog key.
 */
@FunctionalInterface
public interface DialogHandler {

    /**
     * Handles the payload logic for a specific dialog key.
     *
     * @param event   which is involved
     * @param payload of the dialog
     */
    void handle(PlayerCustomClickEvent event, CompoundBinaryTag payload);
}
