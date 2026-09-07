package net.theevilreaper.bounce.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.player.BouncePlayer;

/**
 * Utility class for player-related operations.
 *
 * @version 1.0.0
 * @since 0.1.0
 * @author theEvilReaper
 */
public final class PlayerUtil {

    /**
     * Prepares all online players by setting their level to 0 and starting a round if they are a BouncePlayer.
     */
    public static void preparePlayers() {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.setLevel(0);
            if (onlinePlayer instanceof BouncePlayer bouncePlayer) {
                bouncePlayer.startRound();
            }
        }
    }

    private PlayerUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
