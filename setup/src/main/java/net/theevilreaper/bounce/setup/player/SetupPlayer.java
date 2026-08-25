package net.theevilreaper.bounce.setup.player;

import net.minestom.server.coordinate.BlockVec;
import net.theevilreaper.bounce.common.player.PermissionAwarePlayer;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.Nullable;

public class SetupPlayer extends PermissionAwarePlayer {

    private @Nullable BlockVec leftCorner;
    private @Nullable BlockVec rightCorner;

    public SetupPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    public void setLeftCorner(@Nullable BlockVec leftCorner) {
        this.leftCorner = leftCorner;
    }

    public void setRightCorner(@Nullable BlockVec rightCorner) {
        this.rightCorner = rightCorner;
    }

    public @Nullable BlockVec getRightCorner() {
        return rightCorner;
    }

    public @Nullable BlockVec getLeftCorner() {
        return leftCorner;
    }
}
