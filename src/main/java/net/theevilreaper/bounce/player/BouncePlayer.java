package net.theevilreaper.bounce.player;

import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.theevilreaper.bounce.common.player.PermissionAwarePlayer;

/**
 * The {@link net.minestom.server.entity.Player} implementation used for the actual game. Currently
 * adds nothing beyond {@link PermissionAwarePlayer}'s LuckPerms-backed permission checks; a hook
 * for game-specific player state later.
 */
public final class BouncePlayer extends PermissionAwarePlayer {

    public BouncePlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }
}
