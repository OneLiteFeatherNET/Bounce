package net.theevilreaper.bounce.setup.player;

import net.theevilreaper.bounce.common.player.PermissionAwarePlayer;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

public class SetupPlayer extends PermissionAwarePlayer {

    public SetupPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }
}
